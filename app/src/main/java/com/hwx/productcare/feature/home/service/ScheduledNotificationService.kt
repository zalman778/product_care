package com.hwx.productcare.feature.home.service

import android.app.IntentService
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hwx.productcare.R
import timber.log.Timber

class ScheduledNotificationService: IntentService("com.hwx.product_care.schedule.service") {

    override fun onHandleIntent(intent: Intent?) {

        val value = intent!!.getStringExtra(EXTRA_NOTIFICATION_TEXT)

        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_checked)
            .setContentTitle("Expired value")
            .setContentText(value)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
        val notificationId = 0
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, builder.build())
        }
        Timber.w("AVX: got notification: $value")
    }

    companion object {
        const val CHANNEL_ID = "com.hwx.productcard.channel"
        const val NOTIFICATION_PERIOD = 10000 //10 secs
        const val EXTRA_NOTIFICATION_TEXT = "EXTRA_NOTIFICATION_TEXT"
    }
}