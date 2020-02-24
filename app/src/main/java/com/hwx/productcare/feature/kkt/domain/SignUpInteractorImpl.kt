package com.hwx.productcare.feature.kkt.domain

import android.content.SharedPreferences
import com.hwx.productcare.feature.kkt.repository.SignUpDataRepository
import com.hwx.productcare.provider.PreferencesKeys.KEY_KKT_USERNAME
import com.hwx.productcare.provider.PreferencesKeys.KEY_KKT_PASSWORD
import com.hwx.productcare.provider.PreferencesKeys.SHARED_PREFS_NAME
import com.hwx.productcare.provider.SharedPreferencesProvider
import javax.inject.Inject

class SignUpInteractorImpl @Inject constructor (
    private val signUpDataRepository: SignUpDataRepository,
    private val sharedPreferencesProvider: SharedPreferencesProvider
    ) : SignUpInteractor {

    override fun saveProfilePhone(phone: String) {
        signUpDataRepository.saveProfilePhone(phone)
    }

    override fun saveProfilePassword(password: String) {
        signUpDataRepository.saveProfilePassword(password)
    }

    override fun saveKktProfileData() {
        val sharedPrefs = sharedPreferencesProvider.getSharedPreferences(SHARED_PREFS_NAME, 0)
        val editor: SharedPreferences.Editor = sharedPrefs.edit()
        editor.putString(KEY_KKT_USERNAME, signUpDataRepository.getProfilePhone())
        editor.putString(KEY_KKT_PASSWORD, signUpDataRepository.getProfilePassword())
        editor.apply()

        //cleaning repo
        signUpDataRepository.saveProfilePassword("")
        signUpDataRepository.saveProfilePhone("")
    }

}