package com.hwx.productcare.feature.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.TransitionInflater
import com.hwx.productcare.R
import com.hwx.productcare.feature.home.ui.adapter.ReceiptAdapter
import com.hwx.productcare.feature.home.viewmodel.ProductStorageViewModel
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.ui.BaseFragment
import com.hwx.productcare.ui.MainActivity
import javax.inject.Inject


class ProductStorageFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val productStorageViewModel: ProductStorageViewModel by viewModels { viewModelFactory }

    private lateinit var receiptAdapter: ReceiptAdapter
    private lateinit var productsRecycler: RecyclerView
    private val rvProductsObserver = Observer<List<Receipt>> { handleResponse(it) }


    private fun handleResponse(it: List<Receipt>) {
        receiptAdapter.submitList(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(R.xml.rv_receipt_to_receipt_detail_shared_element_transition)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return view ?: inflater.inflate(
            R.layout.fragment_product_storage,
            container,
            false
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {
        (activity as MainActivity).updateStatusBarColor(R.color.colorHeaderProducts)

        receiptAdapter = ReceiptAdapter(productStorageViewModel)

        view.apply {
            productsRecycler = findViewById(R.id.rvReceipts)
        }

        productsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = receiptAdapter
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        productStorageViewModel.lvReceipts.observe(viewLifecycleOwner, rvProductsObserver)
        productStorageViewModel.lvOnReceiptClick.observe(viewLifecycleOwner, Observer {
            it?.let {
                    val action =
                        ProductStorageFragmentDirections.actionStorageToReceiptDetails(it)
                    findNavController().navigate(action)
                    productStorageViewModel.navigateToReceiptDetailsCompleted()
            }
        })
        productStorageViewModel.lvShowError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isNotEmpty()) {
                    showErrorWithAction(view!!, it) {
                        hideErrorIfShowed()
                    }
                    productStorageViewModel.showErrorCompleted()
                }
            }
        })
        super.onActivityCreated(savedInstanceState)
    }

}