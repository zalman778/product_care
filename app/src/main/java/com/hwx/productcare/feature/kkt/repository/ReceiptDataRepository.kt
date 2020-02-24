package com.hwx.productcare.feature.kkt.repository

import com.hwx.productcare.model.Receipt

interface ReceiptDataRepository {

    fun getReceipt(): Receipt
    fun saveReceipt(receipt: Receipt)

}