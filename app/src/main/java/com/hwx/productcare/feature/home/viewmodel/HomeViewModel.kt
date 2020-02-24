package com.hwx.productcare.feature.home.viewmodel

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hwx.productcare.R
import com.hwx.productcare.feature.home.domain.HomeInteractor
import javax.inject.Inject

class HomeViewModel  @Inject constructor(
    private val homeInteractor: HomeInteractor
) : ViewModel() {

    private val mlvNavigateToProductStorage = MutableLiveData<Boolean>()
    val lvNavigateToProductStorage: LiveData<Boolean>
        get() = mlvNavigateToProductStorage

    private val mlvNavigateToQrScanner = MutableLiveData<Boolean>()
    val lvNavigateToQrScanner: LiveData<Boolean>
        get() = mlvNavigateToQrScanner

    private val mlvNavigateToSignUp = MutableLiveData<Boolean>()
    val lvNavigateToSignUp: LiveData<Boolean>
        get() = mlvNavigateToSignUp

    fun navigateToSignUpCompleted() {
        mlvNavigateToSignUp.value = false
    }

    fun onQrScanClick(view: View) {
        val hasKktProfileData = checkKktProfileData()
        if (!hasKktProfileData) {
            showQrDialog(view.context)
        } else {
            mlvNavigateToQrScanner.value = true
        }
    }

    fun navigateToQrScannerCompleted() {
        mlvNavigateToQrScanner.value = false
    }

    fun navigateToProductStorage(view: View) {
        mlvNavigateToProductStorage.value = true
    }

    fun navigateToProductStorageCompleted() {
        mlvNavigateToProductStorage.value = false
    }


    /**
     * checks for kkt profile in shared prefs
     * todo
     */
    private fun checkKktProfileData(): Boolean {
        return true
//        return homeInteractor.checkKktProfileData()
    }

    private fun showQrDialog(context: Context) {
        val builder = AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle)
        builder.setTitle("Notice")
        builder.setMessage("To retrieve data from online check app needs your KKT profile data? Do you have one? Or Want to register?")
        builder.setPositiveButton("Register") { _, _ ->
            mlvNavigateToSignUp.value = true
        }
        builder.setNeutralButton("Insert") { _, _ ->
            //sendResult(which)
        }
        builder.setNegativeButton("Cancel"){ _, _ ->
            //sendResult(which)
        }
        builder.show()

    }

}