package com.hwx.productcare.provider

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.hwx.productcare.feature.home.service.ScheduledNotificationService
import org.joda.time.DateTime

class AlarmManagerProvider(private val mContext: Context) {

    private val mAlarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun registerNotificationInAlarmManager(
        notificationText: String,
        notificationId: Long,
        keyDateTime: DateTime
    ) {
        val notificationText = "You have expiring items: $notificationText"
        val notificationIntent =
            Intent(mContext, ScheduledNotificationService::class.java)
        notificationIntent.putExtra(
            ScheduledNotificationService.EXTRA_NOTIFICATION_TEXT,
            notificationText
        )
        val pendingIntent = PendingIntent.getService(
            mContext,
            notificationId.toInt(),
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val dateDiff = keyDateTime.millis - DateTime.now().millis
        mAlarmManager[AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + dateDiff] =
            pendingIntent
    }

}
