package com.hwx.productcare.feature.kkt.repository

import com.hwx.productcare.feature.kkt.repository.ReceiptDataRepository
import com.hwx.productcare.model.Receipt
import javax.inject.Inject

class ReceiptDataRepositoryImpl @Inject constructor() :
    ReceiptDataRepository {
    private var mReceipt: Receipt? = null
    override fun getReceipt(): Receipt {
        return mReceipt!!
    }

    override fun saveReceipt(receipt: Receipt) {
        mReceipt = receipt
    }
}