package com.hwx.productcare.feature.kkt.di

import androidx.lifecycle.ViewModel
import com.hwx.productcare.di.ViewModelKey
import com.hwx.productcare.feature.kkt.viewmodel.ProductReceiveViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ProductReceiveViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ProductReceiveViewModel::class)
    abstract fun bindProductReceiveViewModel(productReceiveViewModel: ProductReceiveViewModel): ViewModel


}