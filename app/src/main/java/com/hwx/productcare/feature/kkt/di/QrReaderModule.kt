package com.hwx.productcare.feature.kkt.di

import androidx.lifecycle.ViewModel
import com.hwx.productcare.di.ViewModelKey
import com.hwx.productcare.feature.kkt.domain.*
import com.hwx.productcare.feature.kkt.repository.ReceiptDataRepository
import com.hwx.productcare.feature.kkt.repository.ReceiptDataRepositoryImpl
import com.hwx.productcare.feature.kkt.repository.SignUpDataRepository
import com.hwx.productcare.feature.kkt.repository.SignUpDataRepositoryImpl
import com.hwx.productcare.feature.kkt.ui.fragment.KktPreferencesFragment
import com.hwx.productcare.feature.kkt.ui.fragment.QrCodeReaderFragment
import com.hwx.productcare.feature.kkt.ui.fragment.SignUpConfirmationFragment
import com.hwx.productcare.feature.kkt.ui.fragment.SignUpFragment
import com.hwx.productcare.feature.kkt.viewmodel.KktPreferencesViewModel
import com.hwx.productcare.feature.kkt.viewmodel.QrCodeReaderViewModel
import com.hwx.productcare.feature.kkt.viewmodel.SignUpConfirmationViewModel
import com.hwx.productcare.feature.kkt.viewmodel.SignUpViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class QrReaderModule {

    @Binds
    abstract fun bindQrCodeReaderInteractor(impl: QrCodeReaderInteractorImpl): QrCodeReaderInteractor

    @Binds
    abstract fun bindProductReceiveInteractor(impl: ProductReceiveInteractorImpl): ProductReceiveInteractor

    @Binds
    abstract fun bindSignUpInteractor(impl: SignUpInteractorImpl): SignUpInteractor

    @Binds
    abstract fun bindReceiptDataRepository(impl: ReceiptDataRepositoryImpl): ReceiptDataRepository

    @Binds
    abstract fun binSignUpDataRepository(impl: SignUpDataRepositoryImpl): SignUpDataRepository

    @Binds
    @IntoMap
    @ViewModelKey(SignUpViewModel::class)
    abstract fun bindSignUpViewModel(signUpViewModel: SignUpViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignUpConfirmationViewModel::class)
    abstract fun bindSignUpConfirmationViewModel(signUpConfirmationViewModel: SignUpConfirmationViewModel): ViewModel



    @ContributesAndroidInjector
    abstract fun contributesSignUpFragment(): SignUpFragment

    @ContributesAndroidInjector
    abstract fun contributesSignUpConfirmationFragment(): SignUpConfirmationFragment


    @Binds
    @IntoMap
    @ViewModelKey(QrCodeReaderViewModel::class)
    abstract fun bindQrCodeReaderViewModel(qrCodeReaderViewModel: QrCodeReaderViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributesQrCodeReaderFragment(): QrCodeReaderFragment


    @Binds
    @IntoMap
    @ViewModelKey(KktPreferencesViewModel::class)
    abstract fun bindKktPreferencesViewModel(homeViewModel: KktPreferencesViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributesKktPreferencesFragment(): KktPreferencesFragment


}