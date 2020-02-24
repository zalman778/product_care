package com.hwx.productcare.feature.kkt.di.args

import com.hwx.productcare.feature.kkt.ui.fragment.ProductReceiveFragmentArgs
import com.hwx.productcare.feature.kkt.di.args.ProductReceiveArgsModule.ProductReceiveArgsModuleQualifier.SCANNED_RECEIPT
import com.hwx.productcare.feature.kkt.ui.fragment.ProductReceiveFragment
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ProductReceiveArgsModule  {
    object ProductReceiveArgsModuleQualifier {
        const val SCANNED_RECEIPT = "scanned_receipt"
    }

    @Provides
    @Named(SCANNED_RECEIPT)
    fun provideScannedCode(fragment: ProductReceiveFragment): String {
        return ProductReceiveFragmentArgs.fromBundle(fragment.requireArguments()).scannedReceipt
    }
}
