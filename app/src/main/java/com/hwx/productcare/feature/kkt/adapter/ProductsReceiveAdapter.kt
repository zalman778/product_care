package com.hwx.productcare.feature.kkt.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hwx.productcare.R
import com.hwx.productcare.databinding.RvItemCheckLineBinding
import com.hwx.productcare.external.Animations
import com.hwx.productcare.feature.kkt.viewmodel.ProductItemViewModel
import com.hwx.productcare.feature.kkt.viewmodel.ProductReceiveViewModel
import com.hwx.productcare.model.ReceiptItem
import java.util.*

class ProductsReceiveAdapter() : ListAdapter<ReceiptItem, ProductsReceiveAdapter.ProductsViewHolder>(CheckItemDiff) {

    private object CheckItemDiff : DiffUtil.ItemCallback<ReceiptItem>() {

        override fun areItemsTheSame(oldItem: ReceiptItem, newItem: ReceiptItem): Boolean {
            return  oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ReceiptItem, newItem: ReceiptItem) = oldItem == newItem
    }

    public override fun getItem(position: Int): ReceiptItem {
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsViewHolder {
        val rvItemCheckLineBinding =
            DataBindingUtil.inflate<RvItemCheckLineBinding>(
                LayoutInflater.from(parent.context),
                R.layout.rv_item_check_line,
                parent,
                false)

        return ProductsViewHolder(rvItemCheckLineBinding)
    }

    override fun onBindViewHolder(holder: ProductsViewHolder, position: Int) {
        lateinit var viewModel: ProductItemViewModel

        val onArrowClick: (Boolean) -> Unit = { isExpanded -> run {
            Animations.toggleView(holder.binding.btnListItemExpand, isExpanded)
            if (isExpanded) {
                Animations.expand(holder.binding.rlListItemExpand)
            } else {
                Animations.collapse(holder.binding.rlListItemExpand)
            }
        } }

        val datePickedCallback: (Date) -> Unit = { pickedDate -> run {
            val currentItem = getItem(position)
            currentItem.expiryDate = pickedDate
            viewModel.updateExpiryDate(pickedDate)
        } }


        viewModel =
            ProductItemViewModel(
                onArrowClick,
                datePickedCallback
            )


        val revPosition = itemCount - position
        holder.bindData(getItem(position), revPosition, viewModel)
    }

    override fun onViewAttachedToWindow(holder: ProductsViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: ProductsViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    class ProductsViewHolder(
            val binding: RvItemCheckLineBinding
    ) : RecyclerView.ViewHolder(binding.rlListItemRoot), LifecycleOwner {

        //these are for correct lv updates of item vm..
        private val lifecycleRegistry = LifecycleRegistry(this)

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun onAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun onDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        private val rlProductItemRoot: RelativeLayout = itemView.findViewById(R.id.rlListItemRoot)

        fun bindData(
            receiptItem: ReceiptItem?,
            revPosition: Int,
            viewModel: ProductItemViewModel
        ) {
            rlProductItemRoot.elevation = revPosition.toFloat()
            binding.lifecycleOwner = this
            binding.productItemViewModel = viewModel
            binding.receiptItem = receiptItem
            binding.executePendingBindings()
        }
    }
}