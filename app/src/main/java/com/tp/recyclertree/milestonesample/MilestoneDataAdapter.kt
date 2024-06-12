package com.tp.recyclertree.milestonesample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.LayoutMilestoneOfferItemBinding
import com.tp.recyclertree.databinding.TimelineExactOfferItemBinding
import com.tp.recyclertree.databinding.TimelineGraphIndicatorNormalItemBinding

private const val TAG = "MilestoneDataAdapter"

class MilestoneDataAdapter(
    private val listItems: ArrayList<Int>, private val userStateValue: Int
) : RecyclerView.Adapter<MilestoneDataAdapter.ItemViewHolder>() {

    enum class ItemViewType(val viewTypeId: Int) {
        OFFER_VIEW_EXACT(4), NORMAL(5)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder viewType >> $viewType")

        return when (viewType) {

            ItemViewType.OFFER_VIEW_EXACT.viewTypeId -> {
                val binding = LayoutMilestoneOfferItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MilestoneOfferViewHolder(binding)
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
        return ItemViewType.OFFER_VIEW_EXACT.viewTypeId
    }


    abstract inner class ItemViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(position: Int)

    }


    inner class NormalItemViewHolder(
        private val binding: TimelineGraphIndicatorNormalItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
        }
    }


    inner class MilestoneOfferViewHolder(
        private val binding: LayoutMilestoneOfferItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentValue = listItems[position]
            binding.tvVal.text = "${currentValue}k"
            binding.userStatusView.tvAxisVal.text = "${userStateValue}k"
            binding.userStatusView.tvAxisVal.visibility = View.INVISIBLE

            if (currentValue == userStateValue) {
                binding.userStatusView.root.visibility = View.VISIBLE
                val marginLayoutParams =
                    binding.userStatusView.root.layoutParams as MarginLayoutParams
                marginLayoutParams.marginStart =
                    binding.root.context.resources.getDimension(R.dimen.margin_10).toInt()
            } else {

                var previousMidValueToCompare = Int.MIN_VALUE
                if ((position - 1) >= 0) {
                    val previousValue = listItems[position - 1]
                    previousMidValueToCompare = (previousValue + currentValue) / 2
                }

                var nextMidValueToCompare = Int.MAX_VALUE
                var nextMid: Int? = null

                if ((position + 1) < listItems.size) {
                    val nextValue = listItems[position + 1]
                    nextMidValueToCompare = (nextValue + currentValue) / 2
                }

                if (userStateValue in previousMidValueToCompare until currentValue) {
                    binding.userStatusView.root.visibility = View.VISIBLE
                    binding.userStatusView.tvAxisVal.visibility = View.VISIBLE
                    if ((userStateValue - previousMidValueToCompare) > (currentValue - userStateValue)) {
                        val marginLayoutParams =
                            binding.userStatusView.root.layoutParams as MarginLayoutParams
                        marginLayoutParams.marginStart =
                            binding.root.context.resources.getDimension(R.dimen.margin_5).toInt()
                    }
                } else if (userStateValue in currentValue until nextMidValueToCompare) {
                    binding.userStatusView.root.visibility = View.VISIBLE
                    binding.userStatusView.tvAxisVal.visibility = View.VISIBLE
                    val oneUnitMargin = binding.root.context.resources.getDimension(R.dimen.margin_6).toInt()
                    var unitCount = 4
                    if ((userStateValue - currentValue) < (nextMidValueToCompare - userStateValue)) {
                        unitCount = 3
                    }
                    val marginLayoutParams =
                        binding.userStatusView.root.layoutParams as MarginLayoutParams
                    marginLayoutParams.marginStart = (oneUnitMargin * unitCount)

                } else {
                    binding.userStatusView.root.visibility = View.GONE
                }
            }

        }
    }

}