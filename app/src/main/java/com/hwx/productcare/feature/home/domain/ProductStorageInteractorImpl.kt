package com.hwx.productcare.feature.home.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.hwx.productcare.db.dao.StoreReceiptDao
import com.hwx.productcare.db.dao.StoreReceiptItemDao
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.model.ReceiptItem
import javax.inject.Inject

class ProductStorageInteractorImpl @Inject constructor(
    private val storeReceiptDao: StoreReceiptDao,
    private val storeReceiptItemDao: StoreReceiptItemDao
): ProductStorageInteractor {

    override fun getReceipts(): LiveData<List<Receipt>> {
        return storeReceiptDao.getAll().map {it.map{ it.toModel() }}
    }

    override fun getReceiptsDetails(receiptId: Long): LiveData<List<ReceiptItem>> {
        return storeReceiptItemDao.findLiveByReceiptId(receiptId).map {it.map{ it.toModel() }}
    }

    override suspend fun removeReceipt(receipt: Receipt) {
        storeReceiptDao.deleteByParams(receipt.fiscalDocument, receipt.fiscalMark, receipt.fiscalNumber)
    }

}