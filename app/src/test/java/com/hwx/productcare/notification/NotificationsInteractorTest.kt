package com.hwx.productcare.notification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hwx.productcare.NotificationsMap
import com.hwx.productcare.db.dao.NotificationItemDao
import com.hwx.productcare.db.dao.ProductForNotificationDao
import com.hwx.productcare.db.dao.StoreReceiptItemDao
import com.hwx.productcare.db.entity.NotificationItem
import com.hwx.productcare.db.entity.ProductForNotification
import com.hwx.productcare.db.entity.StoreReceiptItem
import com.hwx.productcare.provider.AlarmManagerProvider
import com.hwx.productcare.provider.SharedPreferencesProvider
import com.hwx.productcare.util.ReceiptItemUtil
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertTrue
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class NotificationsInteractorTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var notificationsInteractor:  NotificationsInteractorImpl

    @MockK
    private lateinit var notificationsDataRepository: NotificationsDataRepository
    @MockK
    private lateinit var notificationItemDao: NotificationItemDao
    @MockK
    private lateinit var productForNotificationDao: ProductForNotificationDao
    @MockK
    private lateinit var storeReceiptItemDao: StoreReceiptItemDao
    @MockK
    private lateinit var sharedPreferencesProvider: SharedPreferencesProvider
    @MockK
    private lateinit var alarmManagerProvider: AlarmManagerProvider

    private val notificationsMap = NotificationsMap()


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        notificationsInteractor = NotificationsInteractorImpl(
            notificationsDataRepository, notificationItemDao, productForNotificationDao, storeReceiptItemDao, sharedPreferencesProvider, alarmManagerProvider
        )
    }

    @Test
    fun `fillNotificationsMapWithDbValues$app`() {
        val notification = NotificationItem(1, "test", DATE_TIME_KEY_1.toDate())

        val existingNotifications = listOf(notification)
        val attachedProducts = listOf(ProductForNotification(1, 1))
        notificationsInteractor.fillNotificationsMapWithDbValues(
            existingNotifications, attachedProducts, notificationsMap
        )
        assertTrue(notificationsMap.isNotEmpty())
        assertTrue(notificationsMap[DATE_TIME_KEY_1]!!.notification!!.text == "test")
    }

    @Test
    fun test_createNotificationsReceiptItems_forDefaultMode() {
        val receiptItem = ReceiptItemUtil.createReceiptItem()
        val receiptItemList = listOf(StoreReceiptItem(receiptItem))
        val beginDate = DateTime().withDate(2020, 2, 1).withTime(BASE_NOTIFY_TIME)
        val endDate = DateTime(receiptItem.expiryDate).withTime(BASE_NOTIFY_TIME)

        notificationsInteractor.createNotificationsReceiptItems(
            notificationsMap, receiptItemList, NotificationFreqMode.DEFAULT, beginDate, endDate, BASE_NOTIFY_TIME
        )

        //for that period should be generated 6 notifications with def mode:
        assertEquals(6, notificationsMap.size)
    }

    @Test
    fun test_createNotificationsReceiptItems_forFreqMode() {
        val receiptItemList = listOf(StoreReceiptItem(ReceiptItemUtil.createReceiptItem()))
        val beginDate = DateTime().withDate(2020, 2, 1).withTime(16, 20, 1, 0)
        val endDate = DateTime().withDate(2020, 2, 5).withTime(BASE_NOTIFY_TIME)

        notificationsInteractor.createNotificationsReceiptItems(
            notificationsMap, receiptItemList, NotificationFreqMode.FREQUENT, beginDate, endDate, BASE_NOTIFY_TIME
        )

        assertEquals(11, notificationsMap.size)
    }

    @Test
    fun test_createNotificationsReceiptItems_forRareMode() {
        val receiptItemList = listOf(StoreReceiptItem(ReceiptItemUtil.createReceiptItem()))
        val beginDate = DateTime().withDate(2020, 2, 1).withTime(16, 20, 1, 0)
        val endDate = DateTime().withDate(2020, 2, 5).withTime(BASE_NOTIFY_TIME)

        notificationsInteractor.createNotificationsReceiptItems(
            notificationsMap, receiptItemList, NotificationFreqMode.RARE, beginDate, endDate, BASE_NOTIFY_TIME
        )
        //01.02 - 1; 3.02 - 1; 05.02 - 2
        assertEquals(4, notificationsMap.size)
    }


    companion object {
        val DATE_TIME_KEY_1 = DateTime().withDate(2020, 2, 2).withTime(16, 20, 1, 0)
        val BASE_NOTIFY_TIME = LocalTime().withHourOfDay(19).withMinuteOfHour(0).withSecondOfMinute(0).withMillisOfSecond(0)
    }
}