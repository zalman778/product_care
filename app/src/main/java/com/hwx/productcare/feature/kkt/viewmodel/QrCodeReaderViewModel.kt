package com.hwx.productcare.feature.kkt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwx.productcare.NetworkResult
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.feature.kkt.domain.QrCodeReaderInteractor
import com.hwx.productcare.feature.kkt.misc.ReceiptCheckDbRow
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class QrCodeReaderViewModel @Inject constructor(
    private val qrCodeReaderInteractor: QrCodeReaderInteractor
) : ViewModel() {

    private var mlvReceiptInDb = MutableLiveData<ReceiptCheckDbRow>()
    val lvReceiptInDb: LiveData<ReceiptCheckDbRow> = mlvReceiptInDb

    private var receiptCheckJob: Job? = null

    @UseExperimental(InternalCoroutinesApi::class)
    fun searchReceiptInHistory(scannedReceipt: String) {
        if (receiptCheckJob?.isActive == true) {
            receiptCheckJob!!.cancel()
        }
        receiptCheckJob = viewModelScope.launch(Dispatchers.IO)  {
            qrCodeReaderInteractor.searchReceiptInHistory(scannedReceipt).collect {
                withContext(Dispatchers.Main) {
                    mlvReceiptInDb.value = it
                } }
        }
    }

}