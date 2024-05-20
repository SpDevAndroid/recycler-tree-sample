package com.tp.recyclertree.viewpagersample

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.databinding.LayoutViewPagerItemBinding

private const val TAG = "ImageViewPager2Adapter"

class ImageViewPager2Adapter(
    private val listItems: ArrayList<Int>
) : RecyclerView.Adapter<ImageViewPager2Adapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder  viewType >> $viewType")
        val binding = LayoutViewPagerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder() position : $position")
        val itemImageId = listItems[position]
        holder.onBind(itemImageId)
    }


    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount  >> ${listItems.size}  ")
        return listItems.size
    }

    inner class ItemViewHolder(
        private val binding: LayoutViewPagerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(itemImageId: Int) {
            Log.d(TAG, "onBind() ")
            binding.ivSample.setImageResource(itemImageId)
        }
    }

}