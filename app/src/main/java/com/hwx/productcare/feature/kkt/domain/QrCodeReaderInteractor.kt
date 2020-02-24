package com.hwx.productcare.feature.kkt.domain

import com.hwx.productcare.feature.kkt.misc.ReceiptCheckDbRow
import kotlinx.coroutines.flow.Flow

interface QrCodeReaderInteractor {
    suspend fun searchReceiptInHistory(scannedReceipt: String): Flow<ReceiptCheckDbRow>
}