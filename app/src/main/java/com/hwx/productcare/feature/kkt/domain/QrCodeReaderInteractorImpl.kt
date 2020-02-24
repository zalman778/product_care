package com.hwx.productcare.feature.kkt.domain

import com.hwx.productcare.db.dao.StoreReceiptDao
import com.hwx.productcare.feature.kkt.misc.ReceiptCheckDbRow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class QrCodeReaderInteractorImpl @Inject constructor(
    private val storeReceiptDao: StoreReceiptDao
    ): BaseKktQrReaderInteractor, QrCodeReaderInteractor {

    override suspend fun searchReceiptInHistory(scannedReceipt: String) = flow {
        val receipt = parseScannedCode(scannedReceipt)
        val foundedReceipt = storeReceiptDao.lookUp(receipt.fiscalNumber, receipt.fiscalDocument, receipt.fiscalMark)
        val result =  foundedReceipt != null
        emit(ReceiptCheckDbRow(result, scannedReceipt))
    }

}