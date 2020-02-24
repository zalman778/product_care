package com.hwx.productcare.di.component

import android.app.Application
import com.hwx.productcare.ProductApplication
import com.hwx.productcare.di.modules.*
import com.hwx.productcare.feature.home.di.HomeModule
import com.hwx.productcare.feature.kkt.di.QrReaderModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(modules = [
    AndroidInjectionModule::class,
    DbModule::class,
    AppModule::class,
    KktApiModule::class,
    HomeModule::class,
    QrReaderModule::class,
    ViewModelFactoryModule::class,
    ActivityBuilderModule::class,
    NotificationsModule::class
])
@Singleton
interface AppComponent: AndroidInjector<ProductApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun dbModule(dbModule: DbModule): Builder
        fun appModule(appModule: AppModule): Builder
        fun kktApiModule(kktApiModule: KktApiModule): Builder


        fun build(): AppComponent
    }

    override fun inject(app: ProductApplication)
}