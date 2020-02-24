package com.hwx.productcare.feature.home.di

import androidx.lifecycle.ViewModel
import com.hwx.productcare.di.ViewModelKey
import com.hwx.productcare.feature.home.domain.HomeInteractor
import com.hwx.productcare.feature.home.domain.HomeInteractorImpl
import com.hwx.productcare.feature.home.domain.ProductStorageInteractor
import com.hwx.productcare.feature.home.domain.ProductStorageInteractorImpl
import com.hwx.productcare.feature.home.ui.fragment.HomeFragment
import com.hwx.productcare.feature.home.ui.fragment.ProductStorageFragment
import com.hwx.productcare.feature.home.viewmodel.HomeViewModel
import com.hwx.productcare.feature.home.viewmodel.ProductStorageViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class HomeModule {

    @Binds
    abstract fun bindProductStorageInteractor(impl: ProductStorageInteractorImpl): ProductStorageInteractor

    @Binds
    abstract fun bindHomeInteractor(impl: HomeInteractorImpl): HomeInteractor

    @Binds
    @IntoMap
    @ViewModelKey(ProductStorageViewModel::class)
    abstract fun bindProductStorageViewModel(productStorageViewModel: ProductStorageViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(homeViewModel: HomeViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributesProductStorageFragment(): ProductStorageFragment

    @ContributesAndroidInjector
    abstract fun contributesHomeFragment(): HomeFragment



}