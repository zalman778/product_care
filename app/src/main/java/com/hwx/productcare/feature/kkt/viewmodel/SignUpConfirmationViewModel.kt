package com.hwx.productcare.feature.kkt.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.hwx.productcare.feature.kkt.domain.SignUpInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpConfirmationViewModel @Inject constructor(
    private val signUpInteractor: SignUpInteractor
) : ViewModel() {

    val mlvPassword = MutableLiveData<String>()

    val mlvSubmitBtnEnabled = MediatorLiveData<Boolean>().apply {
        addSource(mlvPassword) {
            value = it.isNotEmpty()
        }
    }

    private val mlvNavigateToHome = MutableLiveData<Boolean>()
    val lvNavigateToHome: LiveData<Boolean>
        get() = mlvNavigateToHome

    fun navigateToHomeCompleted() {
        mlvNavigateToHome.value = false
    }

    fun onBtnConfirmClick(view: View) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                signUpInteractor.saveProfilePassword(mlvPassword.value!!)
                signUpInteractor.saveKktProfileData()
            }
            .onSuccess {
                withContext(Dispatchers.Main) {
                    mlvNavigateToHome.value = true
                }
            }
        }
    }
}