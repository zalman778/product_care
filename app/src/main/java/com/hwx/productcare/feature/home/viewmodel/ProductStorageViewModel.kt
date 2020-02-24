package com.hwx.productcare.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwx.productcare.feature.home.domain.ProductStorageInteractor
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.notification.NotificationsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class ProductStorageViewModel  @Inject constructor(
      private val productStorageInteractor: ProductStorageInteractor
    , private val notificationsInteractor: NotificationsInteractor
) : ViewModel() {

    val lvReceipts: LiveData<List<Receipt>> = productStorageInteractor.getReceipts()


    private val mlvOnReceiptClick = MutableLiveData<Long?>()
    val lvOnReceiptClick: LiveData<Long?> = mlvOnReceiptClick

    private val mlvShowError = MutableLiveData<String>()
    val lvShowError: LiveData<String> = mlvShowError


    fun navigateToReceiptDetailsCompleted() {
        mlvOnReceiptClick.value = null
    }

    fun showErrorCompleted() {
        mlvShowError.value = ""
    }


    fun onSwipeDelete(receipt: Receipt) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                productStorageInteractor.removeReceipt(receipt)
            }
            .onFailure {
                Timber.e(it)
                //onErrorCallback("Error deleting, please retry")
            }
        }
    }

    fun onItemClick(id: Long?) {
        mlvOnReceiptClick.value = id
    }

    fun onMuteReceipt(item: Receipt) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationsInteractor.handleDisabledNotificationsForReceipt(item.id!!)
        }
    }



}