package com.hwx.productcare.feature.home.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hwx.productcare.R
import com.hwx.productcare.feature.home.ui.adapter.ReceiptDetailsAdapter
import com.hwx.productcare.feature.home.viewmodel.ReceiptDetailsViewModel
import com.hwx.productcare.model.ReceiptItem
import com.hwx.productcare.ui.BaseFragment
import com.hwx.productcare.ui.MainActivity
import javax.inject.Inject


class ReceiptDetailsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val receiptDetailsViewModel: ReceiptDetailsViewModel by viewModels { viewModelFactory }

    private lateinit var receiptDetailsAdapter: ReceiptDetailsAdapter
    private lateinit var productsRecycler: RecyclerView

    private val rvReceiptDetailsObserver = Observer<List<ReceiptItem>> { handleResponse(it) }

    private fun handleResponse(it: List<ReceiptItem>) {
        receiptDetailsAdapter.submitList(it)
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

        receiptDetailsAdapter = ReceiptDetailsAdapter(receiptDetailsViewModel)
        view.apply {
            productsRecycler = findViewById(R.id.rvReceipts)
        }

        productsRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = receiptDetailsAdapter
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        receiptDetailsViewModel.lvReceiptDetails.observe(viewLifecycleOwner, rvReceiptDetailsObserver)
        super.onActivityCreated(savedInstanceState)
    }


}