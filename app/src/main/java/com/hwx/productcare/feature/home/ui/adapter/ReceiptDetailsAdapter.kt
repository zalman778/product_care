package com.hwx.productcare.feature.home.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hwx.productcare.R
import com.hwx.productcare.databinding.RvItemReceiptItemBinding
import com.hwx.productcare.feature.home.viewmodel.ReceiptDetailsViewModel
import com.hwx.productcare.model.ReceiptItem
import kotlinx.android.synthetic.main.rv_item_receipt.view.*

class ReceiptDetailsAdapter(
    private val receiptDetailsViewModel: ReceiptDetailsViewModel
) : ListAdapter<ReceiptItem, ReceiptDetailsAdapter.ReceiptItemViewHolder>(
    CheckItemDiff
) {

    private object CheckItemDiff : DiffUtil.ItemCallback<ReceiptItem>() {

        override fun areItemsTheSame(oldItem: ReceiptItem, newItem: ReceiptItem): Boolean {
            return  oldItem.name == newItem.name && oldItem.sum == newItem.sum
        }

        override fun areContentsTheSame(oldItem: ReceiptItem, newItem: ReceiptItem) = oldItem == newItem
    }

    public override fun getItem(position: Int): ReceiptItem {
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptItemViewHolder {
        val rvItemCheckLineBinding = DataBindingUtil.inflate<RvItemReceiptItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.rv_item_receipt_item,
                parent,
                false)

        return ReceiptItemViewHolder(
            rvItemCheckLineBinding
        )
    }

    override fun onBindViewHolder(holder: ReceiptItemViewHolder, position: Int) {
        val receiptItem = getItem(position)
        val revPosition = itemCount - position
        holder.binding.rightSubMenu.actionMute.setOnClickListener {
            receiptDetailsViewModel.onMuteReceiptItem(receiptItem)
        }
        holder.binding.rlListItemRoot.elevation = revPosition.toFloat()
        holder.bindData(getItem(position), revPosition)
    }


    class ReceiptItemViewHolder(
            val binding: RvItemReceiptItemBinding
    ) : RecyclerView.ViewHolder(binding.rlListItemRoot) {
        private val tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)

        fun bindData(receiptItem: ReceiptItem?, revPosition: Int) {
            //imageView.tint
            receiptItem?.apply {
                tvProductTitle.text = receiptItem.name
            }
        }
    }
}