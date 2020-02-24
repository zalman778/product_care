package com.hwx.productcare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.hwx.productcare.di.component.DaggerAppComponent
import com.hwx.productcare.di.modules.AppModule
import com.hwx.productcare.di.modules.DbModule
import com.hwx.productcare.di.modules.KktApiModule
import com.hwx.productcare.feature.home.service.ScheduledNotificationService
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class ProductApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        createNotificationChannel()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent
                .builder()
                .application(this)
                .appModule(AppModule(applicationContext))
                .kktApiModule(KktApiModule())
                .dbModule(DbModule())
                .build()
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "channel_name"//getString(R.string.channel_name)
            val descriptionText = "channel_desc"//getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(ScheduledNotificationService.CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}