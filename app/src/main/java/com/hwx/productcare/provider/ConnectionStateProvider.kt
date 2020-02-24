package com.hwx.productcare.provider

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


class ConnectionStateProvider(private val mContext: Context) {

    fun isConnectingToInternet(): Boolean {
        val connectivity =
            mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivity != null) {
            val info = connectivity.allNetworkInfo
            if (info != null) for (i in info.indices) if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        }
        return false
    }

}