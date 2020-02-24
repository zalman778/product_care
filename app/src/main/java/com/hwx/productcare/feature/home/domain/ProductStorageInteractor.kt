package com.hwx.productcare.feature.home.domain

import androidx.lifecycle.LiveData
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.model.ReceiptItem

interface ProductStorageInteractor {

    fun getReceipts(): LiveData<List<Receipt>>

    fun getReceiptsDetails(receiptId: Long): LiveData<List<ReceiptItem>>

    suspend fun removeReceipt(receipt: Receipt)

}