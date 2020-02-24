package com.hwx.productcare.feature.kkt.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.hwx.productcare.db.dao.StoreReceiptDao
import com.hwx.productcare.db.dao.StoreReceiptItemDao
import com.hwx.productcare.feature.kkt.repository.FnsRepository
import com.hwx.productcare.feature.kkt.repository.ReceiptDataRepository
import com.hwx.productcare.notification.NotificationsInteractor
import com.hwx.productcare.provider.SharedPreferencesProvider
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductReceiveInteractorImplTest {

    @MockK
    private lateinit var storeReceiptDao: StoreReceiptDao
    @MockK
    private lateinit var storeReceiptItemDao: StoreReceiptItemDao
    @MockK
    private lateinit var fnsRepository: FnsRepository
    @MockK
    private lateinit var receiptDataRepository: ReceiptDataRepository
    @MockK
    private lateinit var notificationsInteractor: NotificationsInteractor
    @MockK
    private lateinit var sharedPreferencesProvider: SharedPreferencesProvider

    lateinit var productReceiveInteractorImpl: ProductReceiveInteractorImpl

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        productReceiveInteractorImpl = ProductReceiveInteractorImpl(storeReceiptDao, storeReceiptItemDao, fnsRepository, receiptDataRepository, notificationsInteractor, sharedPreferencesProvider)
    }

    @Test
    fun `parseScannedCode$app_debug`() {
        val receipt = productReceiveInteractorImpl.parseScannedCode(STUB_QR_DATA)
        assertEquals(9284000100186898, receipt.fiscalNumber)
    }

    companion object {
        const val STUB_QR_DATA = "t=20200126T161500&s=815.30&fn=9284000100186898&i=85465&fp=3674755087&n=1"
    }
}