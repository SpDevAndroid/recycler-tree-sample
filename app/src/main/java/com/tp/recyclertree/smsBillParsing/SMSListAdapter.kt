package com.tp.recyclertree.smsBillParsing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.databinding.LayoutSmsListItemBinding

private const val TAG = "SMSListAdapter"

class SMSListAdapter(
    private val listItems: ArrayList<CreditCardBill>
) : RecyclerView.Adapter<SMSListAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        AppLog.d(TAG, "onCreateViewHolder  viewType >> $viewType")
        val binding = LayoutSmsListItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        AppLog.d(TAG, "onBindViewHolder() position : $position")
        holder.onBind(listItems[position])
    }


    override fun getItemCount(): Int {
        AppLog.d(TAG, "getItemCount  >> ${listItems.size}  ")
        return listItems.size
    }

    inner class ItemViewHolder(
        private val binding: LayoutSmsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(creditCardBill: CreditCardBill) {
            val titleText = "${creditCardBill.senderId} ( ${creditCardBill.card} )"
            binding.tvSenderId.text = titleText
            binding.tvMessage.text = creditCardBill.completeMessage
        }
    }
}