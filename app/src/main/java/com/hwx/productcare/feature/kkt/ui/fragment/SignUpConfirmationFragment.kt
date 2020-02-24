package com.hwx.productcare.feature.kkt.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hwx.productcare.R
import com.hwx.productcare.databinding.FragmentSignupBinding
import com.hwx.productcare.databinding.FragmentSignupConfirmationBinding
import com.hwx.productcare.feature.kkt.viewmodel.SignUpConfirmationViewModel
import com.hwx.productcare.feature.kkt.viewmodel.SignUpViewModel
import com.hwx.productcare.ui.BaseFragment
import timber.log.Timber
import javax.inject.Inject

class SignUpConfirmationFragment : BaseFragment() {

    private lateinit var binding: FragmentSignupConfirmationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val signUpConfirmationViewModel: SignUpConfirmationViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_signup, container, false
        )

        binding.lifecycleOwner = this
        binding.signUpConfirmationViewModel = signUpConfirmationViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        signUpConfirmationViewModel.lvNavigateToHome.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(R.id.navigation_home)
                    signUpConfirmationViewModel.navigateToHomeCompleted()
                }
            }
        })
        super.onActivityCreated(savedInstanceState)
    }



}