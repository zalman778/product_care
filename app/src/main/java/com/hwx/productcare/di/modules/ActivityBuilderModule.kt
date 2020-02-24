package com.hwx.productcare.di.modules

import com.hwx.productcare.di.ActivityScope
import com.hwx.productcare.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(
        modules = [
            ActivityModule::class,
            FragmentBuilderModule::class
        ]
    )
    fun contributeMainActivity(): MainActivity
}