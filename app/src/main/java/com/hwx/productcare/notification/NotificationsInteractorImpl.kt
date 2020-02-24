package com.hwx.productcare.notification

import com.hwx.productcare.NotificationsMap
import com.hwx.productcare.db.dao.NotificationItemDao
import com.hwx.productcare.db.dao.ProductForNotificationDao
import com.hwx.productcare.db.dao.StoreReceiptItemDao
import com.hwx.productcare.db.entity.NotificationItem
import com.hwx.productcare.db.entity.ProductForNotification
import com.hwx.productcare.db.entity.StoreReceiptItem
import com.hwx.productcare.model.Notification
import com.hwx.productcare.model.NotificationAndLinksKeeper
import com.hwx.productcare.provider.AlarmManagerProvider
import com.hwx.productcare.provider.PreferencesKeys
import com.hwx.productcare.provider.PreferencesKeys.KEY_NOTIF_BASE_TIME
import com.hwx.productcare.provider.PreferencesKeys.KEY_NOTIF_FREQ_MODE
import com.hwx.productcare.provider.PreferencesKeys.KEY_NOTIF_NIGHT_END
import com.hwx.productcare.provider.PreferencesKeys.KEY_NOTIF_NIGHT_START
import com.hwx.productcare.provider.SharedPreferencesProvider
import com.hwx.productcare.toLocalDates
import org.joda.time.DateTime
import org.joda.time.Interval
import org.joda.time.LocalTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.HashMap

class NotificationsInteractorImpl @Inject constructor(
    private val notificationsDataRepository: NotificationsDataRepository
    , private val notificationItemDao: NotificationItemDao
    , private val productForNotificationDao: ProductForNotificationDao
    , private val storeReceiptItemDao: StoreReceiptItemDao
    , private val sharedPreferencesProvider: SharedPreferencesProvider
    , private val alarmManagerProvider: AlarmManagerProvider
) : NotificationsInteractor {

    override fun saveFirstPrefsState() {

        notificationsDataRepository.getFirstPrefsState().clear()

        //reading current settings to compare them
        val sharedPrefs =
            sharedPreferencesProvider.getSharedPreferences(PreferencesKeys.SHARED_PREFS_NAME, 0)
        val freqMode =
            NotificationFreqMode.getByValue(sharedPrefs.getString(KEY_NOTIF_FREQ_MODE, "")!!)
        val baseNotifyTimeStr = sharedPrefs.getString(KEY_NOTIF_BASE_TIME, "19:00")!!
        val nightStartStr = sharedPrefs.getString(KEY_NOTIF_NIGHT_START, "23:00")!!
        val nightStopStr = sharedPrefs.getString(KEY_NOTIF_NIGHT_END, "7:00")!!

        val beforePrefsMap = HashMap<String, String>()
        beforePrefsMap[KEY_NOTIF_FREQ_MODE] = freqMode.name
        beforePrefsMap[KEY_NOTIF_BASE_TIME] = baseNotifyTimeStr
        beforePrefsMap[KEY_NOTIF_NIGHT_START] = nightStartStr
        beforePrefsMap[KEY_NOTIF_NIGHT_END] = nightStopStr

        notificationsDataRepository.saveFirstPrefsState(beforePrefsMap)
    }

    override suspend fun handleChangedSetting() {

        val beforePrefsMap = notificationsDataRepository.getFirstPrefsState()
        val sharedPrefs =
            sharedPreferencesProvider.getSharedPreferences(PreferencesKeys.SHARED_PREFS_NAME, 0)

        val newNotifyTimeStr = sharedPrefs.getString(KEY_NOTIF_BASE_TIME, "19:00")!!
        val oldNotifyTimeStr = beforePrefsMap[KEY_NOTIF_BASE_TIME].orEmpty()
        if (newNotifyTimeStr != oldNotifyTimeStr) {
            Timber.w("AVX:detected time change: from $oldNotifyTimeStr to $newNotifyTimeStr")
            handleChangedSettingNotifyTime(newNotifyTimeStr)
        }
    }


    override suspend fun handleInsertedReceiptItems(insertedIdsList: List<Long>) {
        val sharedPrefs =
            sharedPreferencesProvider.getSharedPreferences(PreferencesKeys.SHARED_PREFS_NAME, 0)
        val freqMode =
            NotificationFreqMode.getByValue(sharedPrefs.getString(KEY_NOTIF_FREQ_MODE, "")!!)
        val baseNotifyTimeStr = sharedPrefs.getString(KEY_NOTIF_BASE_TIME, "19:00")!!

        //val nightStartStr = sharedPrefs.getString(KEY_NOTIF_NIGHT_START, "23:00")!!
        //val nightStopStr = sharedPrefs.getString(KEY_NOTIF_NIGHT_END, "7:00")!!

        val baseNotifyTimeParts = baseNotifyTimeStr.split(":")

        val baseNotifyTime = LocalTime.now()
            .withHourOfDay(baseNotifyTimeParts[0].toInt())
            .withMinuteOfHour(baseNotifyTimeParts[1].toInt())
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)

        val newReceiptItemsWithExpiryDate = storeReceiptItemDao
            .findByIdsList(insertedIdsList)
            .filter { it.expiryDate != null }


        if (newReceiptItemsWithExpiryDate.isNotEmpty()) {

            var endDate = Date()
            newReceiptItemsWithExpiryDate.forEach {
                if (it.expiryDate!! > endDate)
                    endDate = it.expiryDate!!
            }

            val beginDateJoda = DateTime.now().withTime(baseNotifyTime)
            val endDateJoda = DateTime(endDate).withTime(baseNotifyTime)

            val existingNotifications =
                notificationItemDao.findByDatePeriod(beginDateJoda.toDate(), endDateJoda.toDate())

            val notificationsMap = NotificationsMap()

            //if notification exists - overriding them, and updating in DB:
            if (existingNotifications.isNotEmpty()) {
                val notificationIds = existingNotifications.map { it.id!! }
                val attachedProducts =
                    productForNotificationDao.findByNotificationIdsList(notificationIds)
                fillNotificationsMapWithDbValues(
                    existingNotifications,
                    attachedProducts,
                    notificationsMap
                )
            }

            createNotificationsReceiptItems(
                notificationsMap,
                newReceiptItemsWithExpiryDate,
                freqMode,
                beginDateJoda,
                endDateJoda,
                baseNotifyTime
            )

            updateNotificationsText(notificationsMap)
            updateDbByNotificationsMap(notificationsMap)
            updateSystemNotificationsByMap(notificationsMap)
        }
    }

    override suspend fun handleDisabledNotificationsForReceipt(receiptId: Long) {
        val receiptItemsList = storeReceiptItemDao.findByReceiptId(receiptId)
        val currentDate = Date()
        val filteredReceiptItemList = receiptItemsList
            .filter { it.expiryDate != null && it.expiryDate!! > currentDate }.map { it.id!! }
        handleDisabledNotificationsForReceiptItems(filteredReceiptItemList)
    }

    override suspend fun handleDisabledNotificationsForReceiptItem(receiptItemId: Long) {
        handleDisabledNotificationsForReceiptItems(listOf(receiptItemId))
    }

    private fun handleChangedSettingNotifyTime(newNotifyTimeStr: String) {

        val newValueTimeParts = newNotifyTimeStr.split(":")
        val newValueTime = LocalTime()
            .withHourOfDay(newValueTimeParts[0].toInt())
            .withMinuteOfHour(newValueTimeParts[1].toInt())
            .withSecondOfMinute(0)
            .withMillisOfSecond(0)

        val allNotifications = notificationItemDao.getAll()
        allNotifications.forEach {
            val newNotificationDateTime = DateTime(it.date).withTime(newValueTime)
            alarmManagerProvider.registerNotificationInAlarmManager(
                it.text,
                it.id!!,
                newNotificationDateTime
            )
            it.date = newNotificationDateTime.toDate()
        }
        notificationItemDao.update(allNotifications)
    }

    /**
     * Registers or updates schedule of notifications with AlarmManager
     */
    private fun updateSystemNotificationsByMap(notificationsMap: NotificationsMap) {

        notificationsMap.forEach {
            alarmManagerProvider.registerNotificationInAlarmManager(
                it.value.notification!!.text,
                it.value.notification!!.id!!,
                it.key
            )
//            if (it.value.isNew) {
//                registerNotificationInAlarmManager(it, alarmManager)
//            } else {
//                //remove existing and create new:
//                val notificationIntent =
//                    Intent(applicationContext, ScheduledNotificationService::class.java)
//                val pendingIntent = PendingIntent.getService(
//                    applicationContext,
//                    it.value.notification!!.id!!.toInt(),
//                    notificationIntent,
//                    0
//                )
//                alarmManager.cancel(pendingIntent)
//
//                registerNotificationInAlarmManager(it, alarmManager)
//            }
        }
    }


    /**
     * Synchronizing db state with notifications map
     */
    private fun updateDbByNotificationsMap(notificationsMap: NotificationsMap) {


        //delete those notifications, who has no any products connected
        //deleting from db
        val toDeleteNotificationListIds = notificationsMap.filterValues { it.links.isEmpty() }
            .map { it.value.notification!!.id!! }
        productForNotificationDao.deleteByNotificationId(toDeleteNotificationListIds)
        notificationItemDao.deleteByIdsList(toDeleteNotificationListIds)

        //deleting from map
        val clearNotificationsMap = notificationsMap.filterValues { it.links.isNotEmpty() }


        //updating existed notifications:
        val toUpdateMapPart = clearNotificationsMap.filter { !it.value.isNew }
        val toUpdateList =
            toUpdateMapPart.map { NotificationItem(it.value.notification!!) }.toList()
        notificationItemDao.update(toUpdateList)
        productForNotificationDao.deleteByNotificationId(toUpdateList.map { it.id!! })

        var toInsertProdByNotification = ArrayList<ProductForNotification>()
        toUpdateMapPart.forEach {
            it.value.links.forEach { lnk ->
                toInsertProdByNotification.add(
                    ProductForNotification(
                        it.value.notification!!.id!!,
                        lnk
                    )
                )
            }
        }
        productForNotificationDao.insert(*toInsertProdByNotification.toTypedArray())

        //inserting new notifications:
        val toInsertMapPart = clearNotificationsMap.filter { it.value.isNew }
        val toInsertArray =
            toInsertMapPart.map { NotificationItem(it.value.notification!!) }.toTypedArray()

        val insertedIdsList = notificationItemDao.insert(*toInsertArray)
        var insertedIdsListIdx = 0
        toInsertProdByNotification = ArrayList()
        toInsertMapPart.forEach {
            val insertedId = insertedIdsList[insertedIdsListIdx]
            it.value.links.forEach { lnk ->
                toInsertProdByNotification.add(ProductForNotification(insertedId, lnk))
            }
            it.value.notification!!.id = insertedId
            insertedIdsListIdx++
        }
        productForNotificationDao.insert(*toInsertProdByNotification.toTypedArray())
    }

    /**
     * Fills map of notifications
     */
    internal fun fillNotificationsMapWithDbValues(
        existingNotifications: List<NotificationItem>,
        attachedProducts: List<ProductForNotification>,
        notificationsMap: NotificationsMap
    ) {
        existingNotifications.forEach {
            val timeKey = DateTime(it.date)
            val targetNotificationKeeper = NotificationAndLinksKeeper()
            notificationsMap[timeKey] = targetNotificationKeeper

            targetNotificationKeeper.notification = it.toModel()
            targetNotificationKeeper.isNew = false
            targetNotificationKeeper.links =
                attachedProducts.filter { pid -> pid.idNotification == it.id }.map { it.idProduct }
                    .toHashSet()
        }
    }


    /**
     * Logic of creating notifications:
     */
    internal fun createNotificationsReceiptItems(
        notificationsMap: NotificationsMap,
        newReceiptItems: List<StoreReceiptItem>,
        freqMode: NotificationFreqMode,
        beginDate: DateTime,
        endDate: DateTime,
        baseNotifyTime: LocalTime
    ) {
        val intervalSequence = Interval(beginDate, endDate.plusDays(1))
        var daysStep = 1
        if (freqMode == NotificationFreqMode.RARE) {
            daysStep = 2
        }
        intervalSequence.toLocalDates(daysStep).forEach { dt ->
            var notificationDateTime = dt.withTime(baseNotifyTime)
            newReceiptItems.forEach { receiptItem ->

                val expiryDateJoda = DateTime(receiptItem.expiryDate).withTime(baseNotifyTime)
                if (expiryDateJoda >= dt) {
                    if (freqMode == NotificationFreqMode.FREQUENT) {
                        //-1 hr
                        notificationDateTime =
                            dt.withTime(baseNotifyTime.minusHours(1))
                        putOrUpdateNotification(notificationsMap, receiptItem, notificationDateTime)
                        //+1 hr
                        notificationDateTime =
                            dt.withTime(baseNotifyTime.plusHours(1))
                        putOrUpdateNotification(notificationsMap, receiptItem, notificationDateTime)
                    } else
                        putOrUpdateNotification(notificationsMap, receiptItem, notificationDateTime)
                }
            }
        }

        //additional notifications on last day: -10hrs(prevTime) and +2 hrs(nextTime):
        val prevDateTime = endDate.minusHours(10)
        val nextDateTime = endDate.plusHours(2)
        newReceiptItems.forEach { receiptItem ->
            val expiryDateJoda = DateTime(receiptItem.expiryDate).withTime(baseNotifyTime)
            if (expiryDateJoda >= prevDateTime) {
                putOrUpdateNotification(notificationsMap, receiptItem, prevDateTime)
            }
            if (expiryDateJoda >= nextDateTime) {
                putOrUpdateNotification(notificationsMap, receiptItem, nextDateTime)
            }
        }

    }

    /**
     *     adds or updates notification in prepared map by ReceiptItem
     */
    private fun putOrUpdateNotification(
        preparedNotificationsMap: NotificationsMap,
        receiptItem: StoreReceiptItem,
        notificationDateTime: DateTime
    ) {
        var targetNotificationKeeper: NotificationAndLinksKeeper? =
            preparedNotificationsMap[notificationDateTime]
        if (targetNotificationKeeper == null) {
            targetNotificationKeeper = NotificationAndLinksKeeper()
            preparedNotificationsMap[notificationDateTime] = targetNotificationKeeper
        }

        if (targetNotificationKeeper.notification == null) {
            targetNotificationKeeper.notification =
                Notification(null, "", notificationDateTime.toDate())
        }
        targetNotificationKeeper.links.add(receiptItem.id!!)
    }


    private fun handleDisabledNotificationsForReceiptItems(filteredReceiptItemListIds: List<Long>) {

        val existingNotifications =
            notificationItemDao.findByReceiptItemsListIds(filteredReceiptItemListIds)

        val notificationsMap: NotificationsMap = HashMap()

        //if notification exists - overriding them, removing spec product..
        if (existingNotifications.isNotEmpty()) {

            val notificationIds = existingNotifications.map { it.id!! }
            val attachedProducts =
                productForNotificationDao.findByNotificationIdsList(notificationIds)

            fillNotificationsMapWithDbValues(
                existingNotifications,
                attachedProducts,
                notificationsMap
            )

            //removing receipt items from map:
            notificationsMap.forEach {
                it.value.links = it.value.links.filter { setIt ->
                    !filteredReceiptItemListIds.contains(setIt)
                }.toHashSet()
            }



            updateNotificationsText(notificationsMap)

            updateDbByNotificationsMap(notificationsMap)

            updateSystemNotificationsByMap(notificationsMap)
        }
    }

    /**
     * Updated notification titles by its products:
     */
    private fun updateNotificationsText(notificationsMap: NotificationsMap) {

        val receiptItemsIdSet = HashSet<Long>()
        notificationsMap.forEach { receiptItemsIdSet.addAll(it.value.links) }
        val receiptItemsList = storeReceiptItemDao.findByIdsList(receiptItemsIdSet.toList())
        val receiptItemsMap = receiptItemsList.associateBy({ it.id!! }, { it })

        notificationsMap.forEach {
            it.value.notification!!.text =
                generateNotificationTitleByProductsIdsList(it.value.links, receiptItemsMap)
        }
    }

    /**
     * Takes every product id in notification, and concatenates it into a title
     */
    private fun generateNotificationTitleByProductsIdsList(
        links: HashSet<Long>,
        receiptItemsList: Map<Long, StoreReceiptItem>
    ): String {
        return links.joinToString(separator = "; ") { receiptItemsList[it]?.name.orEmpty() }
    }


}