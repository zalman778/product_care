package com.hwx.productcare.feature.home.di.args

import androidx.lifecycle.ViewModel
import com.hwx.productcare.di.ViewModelKey
import com.hwx.productcare.feature.home.viewmodel.ReceiptDetailsViewModel
import com.hwx.productcare.feature.kkt.viewmodel.ProductReceiveViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
abstract class ReceiptDetailsViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(ReceiptDetailsViewModel::class)
    abstract fun bindReceiptDetailsViewModel(receiptDetailsViewModel: ReceiptDetailsViewModel): ViewModel


}