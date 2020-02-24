package com.hwx.productcare.feature.kkt.ui.fragment

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.hwx.productcare.NetworkError
import com.hwx.productcare.NetworkResult
import com.hwx.productcare.NetworkSuccess
import com.hwx.productcare.R
import com.hwx.productcare.api.entities.FnsCheck
import com.hwx.productcare.databinding.FragmentReceiveProductBinding
import com.hwx.productcare.feature.home.service.ScheduledNotificationService
import com.hwx.productcare.feature.home.service.ScheduledNotificationService.Companion.EXTRA_NOTIFICATION_TEXT
import com.hwx.productcare.feature.home.service.ScheduledNotificationService.Companion.NOTIFICATION_PERIOD
import com.hwx.productcare.feature.kkt.adapter.ProductsReceiveAdapter
import com.hwx.productcare.feature.kkt.viewmodel.ProductReceiveViewModel
import com.hwx.productcare.model.ReceiptItem
import com.hwx.productcare.ui.BaseFragment
import com.hwx.productcare.ui.MainActivity
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class ProductReceiveFragment : BaseFragment() {

    private lateinit var binding: FragmentReceiveProductBinding

    private lateinit var btnBack: MaterialButton
    private lateinit var btnSave: MaterialButton
    private lateinit var rlRootView: RelativeLayout

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productReceiveViewModel: ProductReceiveViewModel by viewModels { viewModelFactory }
    private lateinit var productsReceiveAdapter: ProductsReceiveAdapter
    private lateinit var productsRecycler: RecyclerView
    private val rvProductsObserver = Observer<NetworkResult<FnsCheck>> { handleResponse(it) }
    private val notificationsObserver = Observer<String> { handleLvNotification(it) }


    private fun handleLvNotification(value: String) {
        Timber.w("AVX: creating notification ($NOTIFICATION_PERIOD) with value = $value")
        val mgr = activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(activity!!, ScheduledNotificationService::class.java)
        notificationIntent.putExtra(EXTRA_NOTIFICATION_TEXT, value)
        val pi = PendingIntent.getService(activity!!, 0, notificationIntent, 0)
        mgr[AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + NOTIFICATION_PERIOD] = pi


    }

    private fun handleResponse(it: NetworkResult<FnsCheck>) {
        when(it) {
            is NetworkSuccess<FnsCheck> -> bindData(it.data!!)
            is NetworkError -> {
                view?.let { view ->
                    showErrorWithAction(view, it.message, this::requestFnsCheck)
                }
            }
        }
    }

    private fun requestFnsCheck() {
        hideErrorIfShowed()
        productReceiveViewModel.getProductsCheck()
    }

    private fun bindData(fnsCheck: FnsCheck) {
        val list = fnsCheck.document.receipt.items.map{ it.toModel() }.toList()
        productReceiveViewModel.setFnsCheckAdditionalData(fnsCheck)
        productsReceiveAdapter.submitList(list)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_receive_product, container, false
        )

        binding.lifecycleOwner = this
        binding.productReceiveViewModel = productReceiveViewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        (activity as MainActivity).updateStatusBarColor(R.color.colorHeaderProducts)

        productsReceiveAdapter = ProductsReceiveAdapter()

        view.apply {
            productsRecycler = findViewById(R.id.rvReceivedProducts)
            btnBack = findViewById(R.id.btn_back)
            btnSave = findViewById(R.id.btn_save_receipt)
            rlRootView = findViewById(R.id.rootHomeLayout)
        }

        productsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = productsReceiveAdapter
        }

        btnBack.setOnClickListener {
            findNavController().popBackStack(R.id.navigation_home, false)
        }

        btnSave.setOnClickListener {
            val itemsList = getAdapterItems()
            productReceiveViewModel.onBtnSaveClick(itemsList)
        }
    }

    private fun getAdapterItems(): List<ReceiptItem> {
        val list = ArrayList<ReceiptItem>()
        for (i in 0 until productsReceiveAdapter.itemCount) {
            list.add(productsReceiveAdapter.getItem(i))
        }
        return list
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        productReceiveViewModel.lvProducts.observe(viewLifecycleOwner, rvProductsObserver)
        productReceiveViewModel.lvNotification.observe(viewLifecycleOwner, notificationsObserver)

        productReceiveViewModel.lvNavigateToHome.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) {
                    findNavController().navigate(R.id.navigation_home)
                    productReceiveViewModel.navigateToHomeCompleted()
                }
            }
        })

        productReceiveViewModel.lvShowError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    showErrorWithAction(view!!, it) {
                        hideErrorIfShowed()
                    }
                    productReceiveViewModel.mlvShowErrorCompleted()
                }
            }
        })

        productReceiveViewModel.getProductsCheck()
        super.onActivityCreated(savedInstanceState)
    }

}