package com.tp.recyclertree.pagersample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.databinding.LayoutViewPagerSampleBindingBinding

private const val TAG = "HighlightBottomItemAdapter"

class ViewPagerSampleAdapter(
    private val listBanners: ArrayList<String>
) : RecyclerView.Adapter<ViewPagerSampleAdapter.HighLightBottomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighLightBottomViewHolder {
        AppLog.d(
            TAG,
            "onCreateViewHolder  viewType >> $viewType"
        )

        val binding =
            LayoutViewPagerSampleBindingBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return HighLightBottomViewHolder(binding = binding)

    }

    override fun onBindViewHolder(holder: HighLightBottomViewHolder, position: Int) {
        AppLog.d(
            TAG,
            "onBindViewHolder() position : $position"
        )
        holder.onBind(position)
    }


    override fun getItemCount(): Int {
        AppLog.d(
            TAG,
            "getItemCount  >> ${listBanners.size}  "
        )
        return listBanners.size
    }


    inner class HighLightBottomViewHolder(
        private val binding: LayoutViewPagerSampleBindingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(position: Int) {
            AppLog.d(TAG, "getItemCount  >> ${listBanners.size}  ")
            val topText = "Top item ${position + 1}"
            val bottomText = "Bottom item ${position + 1}"
            binding.expandView1.text = topText
            binding.expandView3.text = bottomText


        }

    }
}