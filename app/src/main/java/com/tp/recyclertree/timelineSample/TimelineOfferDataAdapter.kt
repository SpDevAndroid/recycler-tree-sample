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
import com.tp.recyclertree.databinding.TimelineGraphIndicatorItemBinding
import com.tp.recyclertree.databinding.TimelineGraphIndicatorNormalItemBinding
import com.tp.recyclertree.databinding.TimelineMidIndicatorItemBinding

private const val TAG = "TimelineOfferDataAdapter"

class TimelineOfferDataAdapter(
    private val listItems: ArrayList<Int>, private val userStateValue: Int
) : RecyclerView.Adapter<TimelineOfferDataAdapter.ItemViewHolder>() {

    enum class ItemViewType(val viewTypeId: Int) {
        EXACT(1),
        MID(2),
        NORMAL(3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder viewType >> $viewType")

        return when (viewType) {
            ItemViewType.MID.viewTypeId -> {
                val binding = TimelineMidIndicatorItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MidItemViewHolder(binding)
            }

            ItemViewType.EXACT.viewTypeId -> {
                val binding = TimelineExactIndicatorItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ExactValueItemViewHolder(binding)
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
        AppLog.d(TAG, "getItemViewType() currentVal : $currentVal prevVal $prevVal userStateValue : $userStateValue")
        return when {
            (userStateValue in (prevVal + 1) until currentVal) -> ItemViewType.MID.viewTypeId
            userStateValue == currentVal -> ItemViewType.EXACT.viewTypeId
            else -> ItemViewType.NORMAL.viewTypeId
        }
    }


    abstract inner class ItemViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(position: Int)

        fun setAxisValue(
            appCompatTextView: AppCompatTextView,
            imageView: AppCompatImageView,
            currentValue: Int
        ) {
            appCompatTextView.text = "${currentValue}k"
            appCompatTextView.visibility = if (currentValue % 100 == 0) {
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

    inner class MidItemViewHolder(
        private val binding: TimelineMidIndicatorItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue)
        }
    }

    inner class ExactValueItemViewHolder(
        private val binding: TimelineExactIndicatorItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue)
        }
    }

    inner class NormalItemViewHolder(
        private val binding: TimelineGraphIndicatorNormalItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            setAxisValue(binding.tvAxisVal, binding.ivAxis, currentValue)
        }
    }

}