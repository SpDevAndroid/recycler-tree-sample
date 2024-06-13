package com.tp.recyclertree.milestonesample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.LayoutMilestoneOfferItemBinding
import com.tp.recyclertree.databinding.TimelineGraphIndicatorNormalItemBinding

private const val TAG = "MilestoneDataAdapter"

class MilestoneDataAdapter(
    private val listItems: ArrayList<MileStoneOffer>, private val userStateValue: Int
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
            val currentMilestoneItem = listItems[position]
            val currentValue = currentMilestoneItem.amt
            binding.tvVal.text = currentMilestoneItem.label
            binding.userStatusView.tvAmt.text = "${userStateValue}k"

            val oneUnitMargin = binding.root.context.resources.getDimension(R.dimen.margin_8).toInt()

            /** Set Margin start for offer item, according to unit spaces between 2 items **/
            var marginStartOffer = binding.root.context.resources.getDimension(R.dimen.margin_3).toInt()
            if(currentMilestoneItem.unitSpace > 1) {
                marginStartOffer = oneUnitMargin * (currentMilestoneItem.unitSpace + 1)
            }
            AppLog.d(TAG, "MilestoneDataAdapter.onBind marginStartOffer : $marginStartOffer currentMilestoneItem : $currentMilestoneItem")
            (binding.timelineOfferView.root.layoutParams as MarginLayoutParams).marginStart = marginStartOffer


            if (currentValue == userStateValue) {
                binding.userStatusView.root.visibility = View.VISIBLE
                val marginLayoutParams = binding.userStatusView.root.layoutParams as MarginLayoutParams
                marginLayoutParams.marginStart = 0
                marginLayoutParams.marginEnd = 0
            } else {

                var previousMidValueToCompare = Int.MIN_VALUE
                if ((position - 1) >= 0) {
                    val previousMilestone = listItems[position - 1]
                    previousMidValueToCompare = previousMilestone.amt + ((currentValue - previousMilestone.amt) / (currentMilestoneItem.unitSpace + 1))
                }

                var nextMidValueToCompare = Int.MAX_VALUE
                if ((position + 1) < listItems.size) {
                    val nextMilestone = listItems[position + 1]
                    nextMidValueToCompare = currentValue + ((nextMilestone.amt - currentValue) / (nextMilestone.unitSpace + 1))
                }

                AppLog.d(TAG, "MilestoneDataAdapter.onBind previousMidValueToCompare $previousMidValueToCompare , currentValue : $currentValue , nextMidValueToCompare : $nextMidValueToCompare")
                when (userStateValue) {
                    in (previousMidValueToCompare + 1)  until currentValue -> {
                        binding.userStatusView.root.visibility = View.VISIBLE
                        var unitCount = 1.5
                        var additionalMargin = marginStartOffer / 2
                        if ((userStateValue - previousMidValueToCompare) < (currentValue - userStateValue)) {
                            unitCount = 3.0
                            additionalMargin = marginStartOffer
                        }

                        (binding.userStatusView.root.layoutParams as MarginLayoutParams).marginEnd = ((oneUnitMargin * unitCount).toInt() + additionalMargin)
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