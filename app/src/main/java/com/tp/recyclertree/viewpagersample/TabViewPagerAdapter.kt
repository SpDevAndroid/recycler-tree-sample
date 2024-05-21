package com.tp.recyclertree.viewpagersample

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.databinding.ViewPagerTabItemBinding

private const val TAG = "TabViewPagerAdapter"

class TabViewPagerAdapter(
    private val listItems: ArrayList<Int>
) : RecyclerView.Adapter<TabViewPagerAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        Log.d(TAG, "onCreateViewHolder  viewType >> $viewType")
        val binding = ViewPagerTabItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
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
        private val binding: ViewPagerTabItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            Log.d(TAG, "onBind() ")
            binding.tvTitle.text = "Tab $position"
            binding.tvUpdate.visibility = if (position == 1) View.VISIBLE else View.GONE
        }
    }

}