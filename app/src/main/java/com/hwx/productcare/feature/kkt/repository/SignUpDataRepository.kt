package com.hwx.productcare.feature.kkt.repository

interface SignUpDataRepository {
    fun saveProfilePhone(phone: String)
    fun saveProfilePassword(password: String)

    fun getProfilePhone(): String
    fun getProfilePassword(): String
}