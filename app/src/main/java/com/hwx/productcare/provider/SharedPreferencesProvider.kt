package com.hwx.productcare.provider

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesProvider(private val mContext: Context) {
    fun getSharedPreferences(name: String?, mode: Int): SharedPreferences {
        return mContext.getSharedPreferences(name, mode)
    }
}
