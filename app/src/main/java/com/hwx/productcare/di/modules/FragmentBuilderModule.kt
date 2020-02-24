package com.hwx.productcare.di.modules

import com.hwx.productcare.feature.home.di.args.ReceiptDetailsArgsModule
import com.hwx.productcare.feature.home.di.args.ReceiptDetailsViewModelsModule
import com.hwx.productcare.feature.home.ui.fragment.ReceiptDetailsFragment
import com.hwx.productcare.feature.kkt.di.ProductReceiveViewModelsModule
import com.hwx.productcare.feature.kkt.di.args.ProductReceiveArgsModule
import com.hwx.productcare.feature.kkt.ui.fragment.ProductReceiveFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {

  @ContributesAndroidInjector(
    modules = [ProductReceiveArgsModule::class, ProductReceiveViewModelsModule::class]
  )
  fun contributesProductReceiveFragment(): ProductReceiveFragment

  @ContributesAndroidInjector(
    modules = [ReceiptDetailsArgsModule::class, ReceiptDetailsViewModelsModule::class]
  )
  fun contributesReceiptDetailsFragment(): ReceiptDetailsFragment

}