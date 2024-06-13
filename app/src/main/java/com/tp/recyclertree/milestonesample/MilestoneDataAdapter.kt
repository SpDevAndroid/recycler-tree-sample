package com.tp.recyclertree.milestonesample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.LayoutMilestoneOfferItemBinding
import com.tp.recyclertree.databinding.LayoutMilestoneSpaceItemBinding
import com.tp.recyclertree.databinding.TimelineGraphIndicatorNormalItemBinding

private const val TAG = "MilestoneDataAdapter"

class MilestoneDataAdapter(
    private val listItems: ArrayList<MileStoneOffer>, private val userStateValue: Int
) : RecyclerView.Adapter<MilestoneDataAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder viewType >> $viewType")

        return when (viewType) {

            MileStoneOffer.ItemType.OFFER.viewId -> {
                val binding = LayoutMilestoneOfferItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MilestoneOfferViewHolder(binding)
            }

            else -> {
                val binding = LayoutMilestoneSpaceItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SpaceItemViewHolder(binding)
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
        return listItems[position].viewType.viewId
    }


    abstract inner class ItemViewHolder(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(position: Int)

    }


    inner class SpaceItemViewHolder(
        private val binding: LayoutMilestoneSpaceItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            var itemBgColor = ContextCompat.getColor(binding.root.context, R.color.purple_200)
            if(position%2 == 0) {
                itemBgColor = ContextCompat.getColor(binding.root.context, R.color.default_glow_color)
            }
            binding.root.setBackgroundColor(itemBgColor)
            val currentMilestoneItem = listItems[position]

            binding.tvVal.text = currentMilestoneItem.label


            val currentValue = currentMilestoneItem.amt

            val oneUnitMargin = binding.root.context.resources.getDimension(R.dimen.margin_1).toInt()

            if (currentValue == userStateValue) {
                binding.userStatusView.root.visibility = View.VISIBLE
                val marginLayoutParams = binding.userStatusView.root.layoutParams as MarginLayoutParams
                marginLayoutParams.marginStart = 0
                marginLayoutParams.marginEnd = 0
            } else {

                var previousMidValueToCompare = Int.MIN_VALUE
                if ((position - 1) >= 0) {
                    val previousMilestone = listItems[position - 1]
                    previousMidValueToCompare = previousMilestone.amt + (currentValue - previousMilestone.amt) / 2
                }

                var nextMidValueToCompare = Int.MAX_VALUE
                if ((position + 1) < listItems.size) {
                    val nextMilestone = listItems[position + 1]
                    nextMidValueToCompare = currentValue + (nextMilestone.amt - currentValue) / 2
                }

                AppLog.d(TAG, "MilestoneDataAdapter.onBind previousMidValueToCompare $previousMidValueToCompare , currentValue : $currentValue , nextMidValueToCompare : $nextMidValueToCompare")
                when (userStateValue) {
                    in (previousMidValueToCompare + 1)  until currentValue -> {
                        binding.userStatusView.root.visibility = View.VISIBLE
                        var unitCount = 1.5
                        if ((userStateValue - previousMidValueToCompare) < (currentValue - userStateValue)) {
                            unitCount = 3.0
                        }

//                        (binding.userStatusView.root.layoutParams as MarginLayoutParams).marginEnd = (oneUnitMargin * unitCount).toInt()
                    }

                    in currentValue  until (nextMidValueToCompare + 1) -> {
                        binding.userStatusView.root.visibility = View.VISIBLE
                        var unitCount = 1.5
                        if ((nextMidValueToCompare - userStateValue) < (userStateValue - currentValue)) {
                            unitCount = 3.0
                        }

//                        (binding.userStatusView.root.layoutParams as MarginLayoutParams).marginStart = (oneUnitMargin * unitCount).toInt()

                    }

                    else -> {
                        binding.userStatusView.root.visibility = View.INVISIBLE
                    }
                }
            }

        }
    }


    inner class MilestoneOfferViewHolder(
        private val binding: LayoutMilestoneOfferItemBinding
    ) : ItemViewHolder(binding.root) {
        override fun onBind(position: Int) {
            val currentMilestoneItem = listItems[position]
            val currentValue = currentMilestoneItem.amt
            binding.tvVal.text = currentMilestoneItem.label
            binding.userStatusView.tvAmt.text = "${userStateValue}k"

            val oneUnitMargin = binding.root.context.resources.getDimension(R.dimen.margin_7).toInt()

            if (currentValue == userStateValue) {
                binding.userStatusView.root.visibility = View.VISIBLE
                val marginLayoutParams = binding.userStatusView.root.layoutParams as MarginLayoutParams
                marginLayoutParams.marginStart = 0
                marginLayoutParams.marginEnd = 0
            } else {

                var previousMidValueToCompare = Int.MIN_VALUE
                if ((position - 1) >= 0) {
                    val previousMilestone = listItems[position - 1]
                    previousMidValueToCompare = previousMilestone.amt + (currentValue - previousMilestone.amt) / 2
                }

                var nextMidValueToCompare = Int.MAX_VALUE
                if ((position + 1) < listItems.size) {
                    val nextMilestone = listItems[position + 1]
                    nextMidValueToCompare = currentValue + (nextMilestone.amt - currentValue) / 2
                }

                AppLog.d(TAG, "MilestoneDataAdapter.onBind previousMidValueToCompare $previousMidValueToCompare , currentValue : $currentValue , nextMidValueToCompare : $nextMidValueToCompare")
                when (userStateValue) {
                    in (previousMidValueToCompare + 1)  until currentValue -> {
                        binding.userStatusView.root.visibility = View.VISIBLE
                        var unitCount = 1.5
                        if ((userStateValue - previousMidValueToCompare) < (currentValue - userStateValue)) {
                            unitCount = 3.0
                        }

                        (binding.userStatusView.root.layoutParams as MarginLayoutParams).marginEnd = (oneUnitMargin * unitCount).toInt()
                    }

                    in currentValue  until (nextMidValueToCompare + 1) -> {
                        binding.userStatusView.root.visibility = View.VISIBLE
                        var unitCount = 1.5
                        if ((nextMidValueToCompare - userStateValue) < (userStateValue - currentValue)) {
                            unitCount = 3.0
                        }

                        (binding.userStatusView.root.layoutParams as MarginLayoutParams).marginStart = (oneUnitMargin * unitCount).toInt()

                    }

                    else -> {
                        binding.userStatusView.root.visibility = View.GONE
                    }
                }
            }

        }
    }

}