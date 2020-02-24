package com.hwx.productcare.feature.kkt.repository

import javax.inject.Inject

class SignUpDataRepositoryImpl @Inject constructor () : SignUpDataRepository {

    private var profilePhone = ""
    private var profilePassword = ""

    override fun saveProfilePhone(phone: String) {
        profilePhone = phone
    }

    override fun saveProfilePassword(password: String) {
        profilePassword = password
    }

    override fun getProfilePhone(): String {
        return profilePhone
    }

    override fun getProfilePassword(): String {
        return profilePassword
    }
}