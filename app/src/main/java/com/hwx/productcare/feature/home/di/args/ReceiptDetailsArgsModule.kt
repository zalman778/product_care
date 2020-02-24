package com.hwx.productcare.feature.home.di.args

import com.hwx.productcare.feature.home.di.args.ReceiptDetailsArgsModule.ReceiptDetailsArgsModuleQualifier.RECEIPT_ID
import com.hwx.productcare.feature.home.ui.fragment.ReceiptDetailsFragment
import com.hwx.productcare.feature.home.ui.fragment.ReceiptDetailsFragmentArgs
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ReceiptDetailsArgsModule  {
    object ReceiptDetailsArgsModuleQualifier {
        const val RECEIPT_ID = "receipt_id"
    }

    @Provides
    @Named(RECEIPT_ID)
    fun provideReceiptId(fragment: ReceiptDetailsFragment): Long {
        return ReceiptDetailsFragmentArgs.fromBundle(fragment.requireArguments()).receiptId
    }
}
