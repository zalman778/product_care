package com.hwx.productcare.feature.kkt.viewmodel

import android.view.View
import androidx.lifecycle.*
import com.hwx.productcare.NetworkError
import com.hwx.productcare.NetworkSuccess
import com.hwx.productcare.feature.kkt.domain.SignUpInteractor
import com.hwx.productcare.feature.kkt.repository.FnsRepository
import com.hwx.productcare.provider.ConnectionStateProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import java.util.regex.Pattern
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    private val fnsRepository: FnsRepository
    , private val signUpInteractor: SignUpInteractor
    , private val connectionStateProvider: ConnectionStateProvider
) : ViewModel() {

    private val mlvSignUpError = MutableLiveData<String>()
    private val mlvVisibilitySignUpError = MutableLiveData<Int>(View.GONE)
    private val mlvVisibilityProgressBar = MutableLiveData<Int>(View.GONE)

    val mlvName = MutableLiveData<String>()
    val mlvEmail = MutableLiveData<String>()
    val mlvPhone = MutableLiveData<String>()

    val lvSignUpError: LiveData<String> = mlvSignUpError
    val lvVisibilitySignUpError: LiveData<Int> = mlvVisibilitySignUpError
    val lvVisibilityProgressBar: LiveData<Int> = mlvVisibilityProgressBar

    private val mlvNavigateToConfirmation = MutableLiveData<Boolean>()
    val lvNavigateToConfirmation: LiveData<Boolean>
        get() = mlvNavigateToConfirmation

    fun navigateToConfirmationCompleted() {
        mlvNavigateToConfirmation.value = false
    }

    val mlvSubmitBtnEnabled = MediatorLiveData<Boolean>().apply {
        addSource(mlvName) {
            value = isFormValid(it, mlvEmail.value.orEmpty(), mlvPhone.value.orEmpty())
        }
        addSource(mlvEmail) {
            value = isFormValid(mlvName.value.orEmpty(), it, mlvPhone.value.orEmpty())
        }
        addSource(mlvPhone) {
            value = isFormValid(mlvName.value.orEmpty(), mlvEmail.value.orEmpty(), it)
        }
    }

    private var signUpJob: Job? = null

    @UseExperimental(InternalCoroutinesApi::class)
    fun onBtnSignUpClick(view: View) {
        mlvSubmitBtnEnabled.value = false
        mlvVisibilityProgressBar.value = View.VISIBLE

        if (signUpJob?.isActive == true) {
            signUpJob!!.cancel()
        }
        signUpJob = viewModelScope.launch(Dispatchers.IO) {
            val isConnected = connectionStateProvider.isConnectingToInternet()
            if (isConnected) {
                fnsRepository.signUp(mlvName.value!!, mlvEmail.value!!, mlvPhone.value!!).collect {
                    when (it) {
                        is NetworkSuccess<Nothing> -> {
                            signUpInteractor.saveProfilePhone(mlvPhone.value!!)
                            withContext(Dispatchers.Main) {
                                mlvNavigateToConfirmation.value = true
                            }
                        }
                        is NetworkError -> {
                            withContext(Dispatchers.Main) {
                                mlvVisibilityProgressBar.value = View.GONE
                                mlvSignUpError.value = it.message
                            }
                        }
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    mlvSignUpError.value = "Остутствует подключение к интернету"
                }
            }
        }
    }

    fun isFormValid(name: String, email: String, phone: String): Boolean {
        return REGEX_EMAIL.matcher(email).matches()
                && name.isNotEmpty()
                && (name.length > 3)
                && (name.length < 16)
                && phone.isNotEmpty()
                && REGEX_PHONE.matcher(phone).matches()
    }

    companion object {
        val REGEX_EMAIL: Pattern = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )

        val REGEX_PHONE: Pattern = Pattern.compile("^\\+\\d+\$")
    }


}