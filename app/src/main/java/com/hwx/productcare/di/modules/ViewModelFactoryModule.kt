package com.hwx.productcare.di.modules

import androidx.lifecycle.ViewModelProvider
import com.hwx.productcare.external.ViewModelFactory
import dagger.Binds
import dagger.Module

/**
 * Module to provide [ViewModelProvider.Factory]
 */
@Module
interface ViewModelFactoryModule {

  @Binds
  fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
