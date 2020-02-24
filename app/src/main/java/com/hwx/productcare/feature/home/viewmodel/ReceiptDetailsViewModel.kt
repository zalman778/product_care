package com.hwx.productcare.feature.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwx.productcare.feature.home.di.args.ReceiptDetailsArgsModule
import com.hwx.productcare.feature.home.domain.ProductStorageInteractor
import com.hwx.productcare.model.ReceiptItem
import com.hwx.productcare.notification.NotificationsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class ReceiptDetailsViewModel  @Inject constructor(
    @Named(ReceiptDetailsArgsModule.ReceiptDetailsArgsModuleQualifier.RECEIPT_ID) private val receiptId: Long
    , private val productStorageInteractor: ProductStorageInteractor
    , private val notificationsInteractor: NotificationsInteractor
) : ViewModel() {

    val lvReceiptDetails: LiveData<List<ReceiptItem>> = productStorageInteractor.getReceiptsDetails(receiptId)

    fun onMuteReceiptItem(item: ReceiptItem) {
        viewModelScope.launch(Dispatchers.IO) {
            notificationsInteractor.handleDisabledNotificationsForReceiptItem(item.id!!)
        }
    }

}