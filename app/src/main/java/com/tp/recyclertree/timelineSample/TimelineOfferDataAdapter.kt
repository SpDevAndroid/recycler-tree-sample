package com.tp.recyclertree.timelineSample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.TimelineExactIndicatorItemBinding
import com.tp.recyclertree.databinding.TimelineExactOfferItemBinding
import com.tp.recyclertree.databinding.TimelineGraphIndicatorNormalItemBinding
import com.tp.recyclertree.databinding.TimelineMidIndicatorItemBinding
import com.tp.recyclertree.databinding.TimelineMidOfferItemBinding

private const val TAG = "TimelineOfferDataAdapter"

class TimelineOfferDataAdapter(
    private val listItems: ArrayList<Int>,
    private val userStateValue: Int,
    private val listOfferItemValues: ArrayList<Int>,
) : RecyclerView.Adapter<TimelineOfferDataAdapter.ItemViewHolder>() {

    enum class ItemViewType(val viewTypeId: Int) {
        USER_STATUS_EXACT(1),
        USER_STATUS_MID(2),
        OFFER_VIEW_MID(3),
        OFFER_VIEW_EXACT(4),
        NORMAL(5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder viewType >> $viewType")

        return when (viewType) {
            ItemViewType.USER_STATUS_MID.viewTypeId -> {
                val binding = TimelineMidIndicatorItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                UserStatusMidValueViewHolder(binding)
            }

            ItemViewType.USER_STATUS_EXACT.viewTypeId -> {
                val binding = TimelineExactIndicatorItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                UserStatusExactValueViewHolder(binding)
            }

            ItemViewType.OFFER_VIEW_MID.viewTypeId -> {
                val binding = TimelineMidOfferItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                OfferViewMidViewHolder(binding)
            }

            ItemViewType.OFFER_VIEW_EXACT.viewTypeId -> {
                val binding = TimelineExactOfferItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                OfferViewExactViewHolder(binding)
            }

            else -> {
                val binding = TimelineGraphIndicatorNormalItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                NormalItemViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() position : $position")
        holder.onBind(position)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount  >> ${listItems.size}  ")
        return listItems.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentVal = listItems[position]
        var prevVal = Int.MIN_VALUE
        if ((position - 1) >= 0) {
            prevVal = listItems[position - 1]
        }
        AppLog.d(
            TAG,
            "getItemViewType() currentVal : $currentVal prevVal $prevVal userStateValue : $userStateValue"
        )
        return when {
            (userStateValue in (prevVal + 1) until currentVal) -> ItemViewType.USER_STATUS_MID.viewTypeId
            userStateValue == currentVal -> ItemViewType.USER_STATUS_EXACT.viewTypeId
            getIfOfferValueInGraphItemMid(currentVal, prevVal) != null -> ItemViewType.OFFER_VIEW_MID.viewTypeId
            getIfOfferValueInGraphExact(currentVal) != null -> ItemViewType.OFFER_VIEW_EXACT.viewTypeId
            else -> ItemViewType.NORMAL.viewTypeId
        }
    }

    private fun getIfOfferValueInGraphItemMid(currentGraphValue : Int , previousGraphValue : Int) : Int? {
        for(offerValue in listOfferItemValues) {
            if(offerValue in (previousGraphValue + 1) until currentGraphValue) {
                return offerValue
            }
        }
        return null
    }

    private fun getIfOfferValueInGraphExact(currentGraphValue : Int): Int? {
        for(offerValue in listOfferItemValues) {
            if(offerValue == currentGraphValue) {
                return offerValue
            }
        }
        return null
    }

    abstract inner class ItemViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(position: Int)

        fun setAxisValue(
            appCompatTextView: AppCompatTextView,
            imageView: AppCompatImageView,
            currentValue: Int,
            position: Int
        ) {
            AppLog.d(TAG , "setAxisValue() absoluteAdapterPosition : $absoluteAdapterPosition currentValue : $currentValue")
            appCompatTextView.text = "${currentValue}k"

            appCompatTextView.visibility = if (currentValue % 100 == 0) {
//            appCompatTextView.visibility = if (position % 2 == 0) {
                imageView.layoutParams.height = (appCompatTextView.context.resources.getDimension(
                    R.dimen.timeline_item_axis_item_indicator_height
                ).toInt() * 2)

                View.VISIBLE
            } else {
                imageView.layoutParams.height = appCompatTextView.context.resources.getDimension(
                    R.dimen.timeline_item_axis_item_indicator_height
                ).toInt()
                View.GONE
            }
        }
    }

    inner class UserStatusMidValueViewHolder(
        private val binding: TimelineMidIndicatorItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue, position)
            binding.tvCurrentUserValue.text = "${userStateValue}k"
        }
    }

    inner class UserStatusExactValueViewHolder(
        private val binding: TimelineExactIndicatorItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue, position)
            binding.tvAxisVal.visibility = View.VISIBLE
        }
    }

    inner class NormalItemViewHolder(
        private val binding: TimelineGraphIndicatorNormalItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue, position)
        }
    }

    inner class OfferViewMidViewHolder(
        private val binding: TimelineMidOfferItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            var prevVal = Int.MIN_VALUE
            if ((position - 1) >= 0) {
                prevVal = listItems[position - 1]
            }
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue, position)
            binding.tvCurrentUserValue.text = "${getIfOfferValueInGraphItemMid(currentValue, prevVal)}k"
        }
    }

    inner class OfferViewExactViewHolder(
        private val binding: TimelineExactOfferItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue, position)
            binding.tvAxisVal.visibility = View.VISIBLE
        }
    }

}