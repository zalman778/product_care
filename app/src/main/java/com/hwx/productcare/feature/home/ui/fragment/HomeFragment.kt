package com.hwx.productcare.feature.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.hwx.productcare.R
import com.hwx.productcare.databinding.FragmentHomeBinding
import com.hwx.productcare.feature.home.viewmodel.HomeViewModel
import com.hwx.productcare.ui.BaseFragment
import javax.inject.Inject

class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val homeViewModel: HomeViewModel by viewModels { viewModelFactory }

    private lateinit var ivReceiptsHeader: ImageView
    private lateinit var tvReceiptsTitle: TextView
    private lateinit var rlReceiptsWrapper: RelativeLayout
    private lateinit var ivSettingsHeader: ImageView
    private lateinit var tvSettingsTitle: TextView
    private lateinit var rlSettingsWrapper: RelativeLayout
    private lateinit var ivQrReaderHeader: ImageView
    private lateinit var tvQrReaderTitle: TextView
    private lateinit var rlQrReaderWrapper: RelativeLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        binding.lifecycleOwner = this
        binding.homeViewModel = homeViewModel

        binding.rlSettingsWrapper.setOnClickListener {
            val extras = FragmentNavigatorExtras(
                ivSettingsHeader to "ivSettingsHeader",
                tvSettingsTitle to "tvSettingsTitle",
                rlSettingsWrapper to "rlSettingsWrapper"
            )
            findNavController().navigate(R.id.navigation_kkt_preferences, null, null, extras)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {

        view.apply {
            ivReceiptsHeader = findViewById(R.id.ivReceiptsHeader)
            tvReceiptsTitle = findViewById(R.id.tvReceiptsTitle)
            rlReceiptsWrapper = findViewById(R.id.rlReceiptsWrapper)
            ivSettingsHeader = findViewById(R.id.ivSettingsHeader)
            tvSettingsTitle = findViewById(R.id.tvSettingsTitle)
            rlSettingsWrapper = findViewById(R.id.rlSettingsWrapper)
            ivQrReaderHeader = findViewById(R.id.ivQrReaderHeader)
            tvQrReaderTitle = findViewById(R.id.tvQrReaderTitle)
            rlQrReaderWrapper = findViewById(R.id.rlQrReaderWrapper)
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        homeViewModel.lvNavigateToProductStorage.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    val extras = FragmentNavigatorExtras(
                        ivReceiptsHeader to "ivReceiptsHeader",
                        tvReceiptsTitle to "tvReceiptsTitle",
                        rlReceiptsWrapper to "rlReceiptsWrapper"
                    )
                    findNavController().navigate(R.id.navigation_storage, null, null, extras)
                    homeViewModel.navigateToProductStorageCompleted()
                }
            }
        })

        homeViewModel.lvNavigateToQrScanner.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    val extras = FragmentNavigatorExtras(
                        ivQrReaderHeader to "ivQrReaderHeader",
                        tvQrReaderTitle to "tvQrReaderTitle",
                        rlQrReaderWrapper to "rlQrReaderWrapper"
                    )
                    findNavController().navigate(R.id.navigation_qr_scanner, null, null, extras)
                    homeViewModel.navigateToQrScannerCompleted()
                }
            }
        })

        homeViewModel.lvNavigateToSignUp.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(R.id.navigation_signup)
                    homeViewModel.navigateToSignUpCompleted()
                }
            }
        })

        super.onActivityCreated(savedInstanceState)
    }

}
