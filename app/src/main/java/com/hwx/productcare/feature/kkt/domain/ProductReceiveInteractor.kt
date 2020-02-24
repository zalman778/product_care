package com.hwx.productcare.feature.kkt.domain

import com.hwx.productcare.NetworkResult
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.model.ReceiptItem
import kotlinx.coroutines.flow.Flow

interface ProductReceiveInteractor {

    /**
     * saves receipt in db
     */
    suspend fun saveReceipt(items: List<ReceiptItem>)

    suspend fun getFnsCheck(scannedReceipt: String): Flow<NetworkResult<FnsCheck>>

    fun setFnsCheckAdditionalData(fnsCheck: FnsCheck)
}