package com.hwx.productcare.feature.home.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hwx.productcare.R
import com.hwx.productcare.databinding.RvItemReceiptBinding
import com.hwx.productcare.feature.home.viewmodel.ProductStorageViewModel
import com.hwx.productcare.model.Receipt
import com.hwx.productcare.util.DateUtil
import kotlinx.android.synthetic.main.rv_item_receipt.view.*

class ReceiptAdapter(
    private val productStorageViewModel: ProductStorageViewModel
) : ListAdapter<Receipt, ReceiptAdapter.ReceiptViewHolder>(
    CheckItemDiff
) {

    private object CheckItemDiff : DiffUtil.ItemCallback<Receipt>() {

        override fun areItemsTheSame(oldItem: Receipt, newItem: Receipt): Boolean {
            return  oldItem.fiscalNumber == newItem.fiscalNumber && oldItem.fiscalMark == newItem.fiscalMark && oldItem.fiscalDocument == newItem.fiscalDocument
        }

        override fun areContentsTheSame(oldItem: Receipt, newItem: Receipt) = oldItem == newItem
    }

    public override fun getItem(position: Int): Receipt {
        return super.getItem(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiptViewHolder {
        val rvItemCheckLineBinding = DataBindingUtil.inflate<RvItemReceiptBinding>(
                LayoutInflater.from(parent.context),
                R.layout.rv_item_receipt,
                parent,
                false)

        return ReceiptViewHolder(
            rvItemCheckLineBinding
        )
    }

    override fun onBindViewHolder(holder: ReceiptViewHolder, position: Int) {

        val receipt = getItem(position)
        holder.binding.rlListItemPrimary.setOnClickListener {
            productStorageViewModel.onItemClick(receipt.id)
        }
        holder.binding.rightSubMenu.actionDel.setOnClickListener {
            productStorageViewModel.onSwipeDelete(receipt)
        }
        holder.binding.rightSubMenu.actionMute.setOnClickListener {
            productStorageViewModel.onMuteReceipt(receipt)
        }

        val revPosition = itemCount - position
        holder.binding.rlListItemRoot.elevation = revPosition.toFloat()
        holder.bindData(getItem(position))
    }


    class ReceiptViewHolder(
            val binding: RvItemReceiptBinding
    ) : RecyclerView.ViewHolder(binding.rlListItemRoot) {
        private val tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)

        fun bindData(receipt: Receipt?) {
            //imageView.tint
            receipt?.apply {
                val title = DateUtil.toString(receipt.date, DateUtil.BEAUTY_RECEIPT_DATE_FORMAT) + ": ("+receipt.summ+") rub."
                tvProductTitle.text = title
            }
        }
    }
}