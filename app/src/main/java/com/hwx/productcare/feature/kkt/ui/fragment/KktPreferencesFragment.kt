package com.hwx.productcare.feature.kkt.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceFragmentCompat
import androidx.transition.TransitionInflater
import com.hwx.productcare.R
import com.hwx.productcare.feature.kkt.viewmodel.KktPreferencesViewModel
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class KktPreferencesFragment : PreferenceFragmentCompat(),
    HasAndroidInjector {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val kktPreferencesViewModel: KktPreferencesViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context)
                .inflateTransition(R.xml.rv_receipt_to_receipt_detail_shared_element_transition)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        view!!.setBackgroundColor(resources.getColor(R.color.colorWhite))
        return view
    }

    override fun onStart() {
        super.onStart()
        kktPreferencesViewModel.saveFirstPrefsState()
    }

    override fun onStop() {
        kktPreferencesViewModel.handleChangedSetting()
        super.onStop()
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.kkt_preferences, rootKey)
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    @Inject
    @JvmField
    var androidInjector: DispatchingAndroidInjector<Any>? = null

    override fun androidInjector(): AndroidInjector<Any> {
        return androidInjector!!
    }

}