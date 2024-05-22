package com.tp.recyclertree.timelineSample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.TimelineGraphIndicatorItemBinding

private const val TAG = "TimelineAdapter"

class TimelineGraphAdapter(
    private val listItems: ArrayList<Int>
) : RecyclerView.Adapter<TimelineGraphAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder  viewType >> $viewType")

        val binding = TimelineGraphIndicatorItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding = binding)

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() position : $position")
        holder.onBind(position)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount  >> ${listItems.size}  ")
        return listItems.size
    }


    inner class ItemViewHolder(
        private val binding: TimelineGraphIndicatorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(position: Int) {
            val currentValue = listItems[position]
            binding.tvAxisVal.text = "${currentValue}k"
            binding.tvAxisVal.visibility =
                if (currentValue % 100 == 0) {
                    binding.ivAxis.layoutParams.height = (binding.root.context.resources.getDimension(R.dimen.timeline_item_axis_item_indicator_height).toInt() * 2)

                    View.VISIBLE
                } else {
                    binding.ivAxis.layoutParams.height = binding.root.context.resources.getDimension(R.dimen.timeline_item_axis_item_indicator_height).toInt()
                    View.GONE
                }
        }
    }

}