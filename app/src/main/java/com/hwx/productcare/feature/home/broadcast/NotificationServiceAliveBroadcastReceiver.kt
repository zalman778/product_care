package com.hwx.productcare.feature.home.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.hwx.productcare.feature.home.service.ScheduledNotificationService
import timber.log.Timber

class NotificationServiceAliveBroadcastReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if ("android.intent.action.BOOT_COMPLETED" == intent!!.action) {
            val serviceLauncher = Intent(context, ScheduledNotificationService::class.java)
            context!!.startService(serviceLauncher)
            Timber.w("AVX: Service loaded at start")
        }
    }

}