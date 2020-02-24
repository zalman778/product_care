package com.hwx.productcare.feature.kkt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwx.productcare.notification.NotificationsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class KktPreferencesViewModel @Inject constructor(
    private val notificationsInteractor: NotificationsInteractor
) : ViewModel() {

    fun saveFirstPrefsState(){
        notificationsInteractor.saveFirstPrefsState()
    }

    fun handleChangedSetting() {

        viewModelScope.launch( Dispatchers.IO) {
            try {
                notificationsInteractor.handleChangedSetting()
            } catch (e: Exception) {
                Timber.e("Error on save")
            }
        }

    }

}