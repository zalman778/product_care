package com.hwx.productcare.feature.kkt.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwx.productcare.NetworkResult
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.feature.kkt.di.args.ProductReceiveArgsModule
import com.hwx.productcare.feature.kkt.domain.ProductReceiveInteractor
import com.hwx.productcare.model.ReceiptItem
import com.hwx.productcare.provider.ConnectionStateProvider
import com.hwx.productcare.util.DateUtil
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named


class ProductReceiveViewModel @Inject constructor(
        @Named(ProductReceiveArgsModule.ProductReceiveArgsModuleQualifier.SCANNED_RECEIPT) private val scannedReceipt: String,
        private val productReceiveInteractor: ProductReceiveInteractor,
        private val connectionStateProvider: ConnectionStateProvider
) : ViewModel() {

    private var fnsCheckJob: Job? = null
    private var mlvProducts = MutableLiveData<NetworkResult<FnsCheck>>()
    val lvProducts: LiveData<NetworkResult<FnsCheck>> = mlvProducts

    private var mlvNotification = MutableLiveData<String>()
    val lvNotification: LiveData<String> = mlvNotification


    private val mlvNavigateToHome = MutableLiveData<Boolean>()
    val lvNavigateToHome: LiveData<Boolean> = mlvNavigateToHome

    private val mlvShowError = MutableLiveData<String>()
    val lvShowError: LiveData<String> = mlvShowError

    fun navigateToHomeCompleted() {
        mlvNavigateToHome.value = false
    }

    fun mlvShowErrorCompleted() {
        mlvShowError.value = ""
    }


    @UseExperimental(InternalCoroutinesApi::class)
    fun getProductsCheck() {
        if (fnsCheckJob?.isActive == true) {
            fnsCheckJob!!.cancel()
        }
        fnsCheckJob = viewModelScope.launch(Dispatchers.IO) {
            val isConnected = connectionStateProvider.isConnectingToInternet()
            if (isConnected) {
                productReceiveInteractor.getFnsCheck(scannedReceipt).collect {
                    withContext(Dispatchers.Main) {
                        mlvProducts.value = it
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    mlvShowError.value = "Остутствует подключение к интернету"
                }
            }
        }
    }

    fun onBtnSaveClick(items: List<ReceiptItem>) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                productReceiveInteractor.saveReceipt(items)
            }
            .onFailure {
                Timber.e(it)
                withContext(Dispatchers.Main) {
                    mlvShowError.value = "Error while saving, please rescan QR"
                }
            }
            .onSuccess {
                checkForExpiryItemsAndScheduleSignal(items)
                withContext(Dispatchers.Main) {
                    mlvNavigateToHome.value = true
                }
            }
        }
    }

    private suspend fun checkForExpiryItemsAndScheduleSignal(items: List<ReceiptItem>) {
        val itemsConcat = items
            .filter { it.expiryDate != null }
            .joinToString { it.name +" "+"Exires: "+DateUtil.toString(it.expiryDate, DateUtil.RECEIPT_DATE_FORMAT) }
        if (itemsConcat.isNotEmpty()) {
            val notificationText = "You have expiring items: $itemsConcat"

            withContext(Dispatchers.Main) {
                mlvNotification.value = notificationText
            }
        }

    }

    fun setFnsCheckAdditionalData(fnsCheck: FnsCheck) {
        productReceiveInteractor.setFnsCheckAdditionalData(fnsCheck)
    }

}