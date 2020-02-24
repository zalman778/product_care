package com.hwx.productcare.di.modules


import com.hwx.productcare.notification.NotificationsDataRepository
import com.hwx.productcare.notification.NotificationsDataRepositoryImpl
import com.hwx.productcare.notification.NotificationsInteractor
import com.hwx.productcare.notification.NotificationsInteractorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class NotificationsModule {

    @Binds
    abstract fun bindNotificationsInteractor(impl: NotificationsInteractorImpl): NotificationsInteractor

    @Binds
    abstract fun bindNotificationsDataRepository(impl: NotificationsDataRepositoryImpl): NotificationsDataRepository

}