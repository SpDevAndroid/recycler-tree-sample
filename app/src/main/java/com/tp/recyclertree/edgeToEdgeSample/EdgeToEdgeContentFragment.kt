package com.tp.recyclertree.edgeToEdgeSample

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.CategoryTabItemBinding
import com.tp.recyclertree.databinding.FragmentEdgeToEdgeContentBinding
import com.tp.recyclertree.pagersample.HorizontalMarginItemDecoration
import com.tp.recyclertree.pagersample.ViewPagerSampleAdapter


private const val TAG = ""
class EdgeToEdgeContentFragment : Fragment() {

    private var _binding: FragmentEdgeToEdgeContentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEdgeToEdgeContentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPagerWithTabs()
    }

    private fun setupViewPagerWithTabs() {
        // Sample data
        val items = arrayListOf("Page 1", "Page 2", "Page 3", "Page 4", "Page 5")

        val viewPager = binding.viewPager

        // Set adapter
        val adapter = ViewPagerSampleAdapter(items)
        viewPager.adapter = adapter

        // Configure ViewPager2 for peek effect
        viewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }

        // Get RecyclerView from ViewPager2
        val recyclerView = viewPager.getChildAt(0) as RecyclerView
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        // Set padding for peek effect (16dp on each side)
        val peekOffset = resources.getDimension(R.dimen.margin_6).toInt()
        viewPager.setPadding(peekOffset, 0, peekOffset, 0)

        // Add page margin between items (8dp spacing)
        val pageMargin = resources.getDimension(R.dimen.margin_3).toInt()
        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(pageMargin))

//        // Optional: Add scale effect for better visual
//        compositePageTransformer.addTransformer { page, position ->
//            val r = 1 - kotlin.math.abs(position)
//            page.scaleY = 0.85f + r * 0.15f
//        }

        viewPager.setPageTransformer(compositePageTransformer)

        // TabLayout integration
        TabLayoutMediator(binding.tlCategories, binding.viewPager) { tab, position ->
            val tabBinding = CategoryTabItemBinding.inflate(layoutInflater, tab.view, false)
            tab.customView = tabBinding.root
            tabBinding.tvTitle.text = items[position]
        }.attach()

        // Optional: tab margins
        setTabsMargin(binding.tlCategories)
    }




    // Optional: tab margins
    private fun setTabsMargin(tabLayout: TabLayout) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            val marginEnd = resources.getDimensionPixelOffset(R.dimen.margin_2)
            var marginStart = 0
            if (i == 0) {
                marginStart = resources.getDimensionPixelOffset(R.dimen.margin_3)
            }
            p.setMargins(marginStart, 0, marginEnd, 0)
            tab.requestLayout()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}