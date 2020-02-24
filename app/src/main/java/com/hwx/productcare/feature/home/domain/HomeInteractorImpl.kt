package com.hwx.productcare.feature.home.domain

import com.hwx.productcare.provider.PreferencesKeys.KEY_KKT_USERNAME
import com.hwx.productcare.provider.PreferencesKeys.KEY_KKT_PASSWORD
import com.hwx.productcare.provider.PreferencesKeys.SHARED_PREFS_NAME
import com.hwx.productcare.provider.SharedPreferencesProvider
import javax.inject.Inject

class HomeInteractorImpl @Inject constructor (
    private val sharedPreferencesProvider: SharedPreferencesProvider

) : HomeInteractor {

    override fun checkKktProfileData(): Boolean {
        val sharedPrefs = sharedPreferencesProvider.getSharedPreferences(SHARED_PREFS_NAME, 0)
        val kktName = sharedPrefs.getString(KEY_KKT_USERNAME, "").orEmpty()
        val kktPassword = sharedPrefs.getString(KEY_KKT_PASSWORD, "").orEmpty()
        return kktName.isNotEmpty() && kktPassword.isNotEmpty()
    }

}