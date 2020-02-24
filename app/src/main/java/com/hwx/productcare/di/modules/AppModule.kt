package com.hwx.productcare.di.modules

import android.content.Context
import com.hwx.productcare.provider.AlarmManagerProvider
import com.hwx.productcare.provider.ConnectionStateProvider
import com.hwx.productcare.provider.SharedPreferencesProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class AppModule constructor(private val context: Context) {

    @Provides
    @Singleton
    fun provideAppContext() = context

    @Provides
    @Singleton
    fun provideSharedPreferences(mContext: Context) = SharedPreferencesProvider(mContext)

    @Provides
    @Singleton
    fun provideAlarmManagerProvider(mContext: Context) = AlarmManagerProvider(mContext)

    @Provides
    @Singleton
    fun provideConnectionStateProvider(mContext: Context) = ConnectionStateProvider(mContext)

}