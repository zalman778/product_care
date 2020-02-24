package com.hwx.productcare.feature.kkt.domain

interface SignUpInteractor {

    fun saveProfilePhone(phone: String)

    fun saveProfilePassword(password: String)

    fun saveKktProfileData()
}