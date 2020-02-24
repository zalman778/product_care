package com.hwx.productcare.feature.kkt.domain

import com.hwx.productcare.NetworkResult
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.db.dao.StoreReceiptDao
import com.hwx.productcare.db.dao.StoreReceiptItemDao
import com.hwx.productcare.db.entity.StoreReceipt
import com.hwx.productcare.db.entity.StoreReceiptItem
import com.hwx.productcare.feature.kkt.repository.FnsRepository
import com.hwx.productcare.feature.kkt.repository.ReceiptDataRepository
import com.hwx.productcare.model.ReceiptItem
import com.hwx.productcare.notification.NotificationsInteractor
import com.hwx.productcare.provider.PreferencesKeys
import com.hwx.productcare.provider.SharedPreferencesProvider
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProductReceiveInteractorImpl @Inject constructor(
    private val storeReceiptDao: StoreReceiptDao,
    private val storeReceiptItemDao: StoreReceiptItemDao,
    private val fnsRepository: FnsRepository,
    private val receiptDataRepository: ReceiptDataRepository,
    private val notificationsInteractor: NotificationsInteractor,
    private val sharedPreferencesProvider: SharedPreferencesProvider
) : BaseKktQrReaderInteractor, ProductReceiveInteractor {

    override fun setFnsCheckAdditionalData(fnsCheck: FnsCheck) {
        val shopTitle = fnsCheck.document.receipt.retailPlace ?: fnsCheck.document.receipt.user
        receiptDataRepository.getReceipt().shopTitle = shopTitle
    }

    override suspend fun saveReceipt(items: List<ReceiptItem>) {
        val receipt = receiptDataRepository.getReceipt()
        val receiptId = storeReceiptDao.insert(StoreReceipt(receipt))
        val itemsCollection = items
            .map { StoreReceiptItem(it) }
            .toTypedArray()

        itemsCollection.forEach {
                it.idReceipt = receiptId.toInt()
            }

        val insertedIdsList = storeReceiptItemDao.insertAll(*itemsCollection)
        notificationsInteractor.handleInsertedReceiptItems(insertedIdsList)
    }

    override suspend fun getFnsCheck(scannedReceipt: String): Flow<NetworkResult<FnsCheck>> {
        val receipt = parseScannedCode(scannedReceipt)
        receiptDataRepository.saveReceipt(receipt)

        val sharedPrefs = sharedPreferencesProvider.getSharedPreferences(PreferencesKeys.SHARED_PREFS_NAME, 0)
        val kktName = sharedPrefs.getString(PreferencesKeys.KEY_KKT_USERNAME, "").orEmpty()
        val kktPassword = sharedPrefs.getString(PreferencesKeys.KEY_KKT_PASSWORD, "").orEmpty()

        return fnsRepository.getFnsCheck(receipt, kktName, kktPassword)
    }
}