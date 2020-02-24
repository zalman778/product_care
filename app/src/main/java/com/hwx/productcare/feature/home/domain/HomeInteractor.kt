package com.hwx.productcare.feature.home.domain

interface HomeInteractor {
    /**
     * Check for KKT service login data in shared Prefs
     */
    fun checkKktProfileData(): Boolean
}