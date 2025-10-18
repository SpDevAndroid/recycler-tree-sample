package com.tp.recyclertree.edgeToEdgeSample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tp.recyclertree.AppLog
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

        setAdapter()
    }

    private fun setAdapter() {
        /** Set Tab selection listener to handle Tab UI changes on tab when tab is selected or unselected **/
        binding.tlCategories.addOnTabSelectedListener(onTabSelectedListener)

        val listTemp = arrayListOf("Tab 1", "Tab 2", "Tab 3")
        /** Set Highlight bottom item view pager adapter**/
        val sampleAdapter = ViewPagerSampleAdapter(listTemp)
        binding.vpDataContainer.adapter = sampleAdapter

        setItemDecorationTabViewPager(binding.vpDataContainer)

        setUpTabsForViewPager(listTemp)
        setPageChangeCallback(binding.vpDataContainer)
    }

    private fun setUpTabsForViewPager(listTemp : ArrayList<String>) {
        TabLayoutMediator(
            binding.tlCategories,
            binding.vpDataContainer,
            false,
            false
        ) { tab, position ->
            val bindingTab = CategoryTabItemBinding.inflate(layoutInflater, tab.view, false)
            tab.customView = bindingTab.root
            bindingTab.tvTitle.text = listTemp.get(position)
        }.attach()

        /** Set tabs margin after tabs value is set up **/
        setTabsMargin(binding.tlCategories)
    }


    /** Method to set up tab margin
     * @Note Call this method after tabs has been added
     * **/
    private fun setTabsMargin(tabLayout: TabLayout) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            val marginEnd = resources.getDimension(R.dimen.margin_2)
            var marginStart = 0

            /** Add Extra margin to start of first item only , Design team requirement : TIMPR-16160 **/
            if (i == 0) {
                marginStart = resources.getDimension(R.dimen.margin_3).toInt()
            }
            p.setMargins(marginStart, 0, marginEnd.toInt(), 0)
            tab.requestLayout()
        }
    }

    /** Handle Tab title color in case when tab selected or when it is unselected  **/
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab?) {
            setTextAndIconColor(tab, R.color.tb_text_color_6)
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
            setTextAndIconColor(tab, R.color.tb_text_color_1)
        }

        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        fun setTextAndIconColor(tab: TabLayout.Tab?, colorResId: Int) {
            val tvTitle: AppCompatTextView? = tab?.customView?.findViewById(R.id.tv_title)
            context?.let { contextRef ->
                AppLog.d(TAG, "onTabSelectedListener onTabSelected()")
                val color = ContextCompat.getColor(contextRef, colorResId)
                tvTitle?.setTextColor(color)
            }
        }

    }

    private fun setItemDecorationTabViewPager(viewPager: ViewPager2) {

        // ✅ Show next/previous items partially
        val nextItemVisiblePx = resources.getDimensionPixelOffset(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimensionPixelOffset(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx

        // Turn off clipping so adjacent pages are visible
        viewPager.clipToPadding = false
        viewPager.clipChildren = false
        viewPager.offscreenPageLimit = 3

        val recyclerView = viewPager.getChildAt(0) as RecyclerView
        recyclerView.clipToPadding = false
        recyclerView.clipChildren = false
        recyclerView.overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        // ✅ Add margin between pages
        val marginTransformer = MarginPageTransformer(
            resources.getDimensionPixelOffset(R.dimen.margin_4)
        )

        // ✅ Add horizontal translation for peek effect
        val peekTransformer = ViewPager2.PageTransformer { page, position ->
            page.translationX = -pageTranslationX * position
        }

        val composite = CompositePageTransformer()
        composite.addTransformer(marginTransformer)
        composite.addTransformer(peekTransformer)
        viewPager.setPageTransformer(composite)

        // You need to retain one page on each side so that the next and previous items are visible
//        viewPager.offscreenPageLimit = 2
//        val nextItemSpacePX =
//            binding.root.context.resources.getDimension(R.dimen.event_vp_next_item_view)
//        AppLog.d(TAG, "ViewPager2.PageTransformer nextItemViewPX: $nextItemSpacePX")
//
//        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
//            AppLog.d(TAG, "ViewPager2.PageTransformer called: $position page: ${page.tag}")
//            if (position > 0) {
//                val calc = nextItemSpacePX * position
//                AppLog.d(TAG, "ViewPager2.PageTransformer calc: $calc")
//                page.translationX = -calc
//            }
//        }
//        viewPager.setPageTransformer(pageTransformer)
//
//        viewPager.context?.let {
//            // The ItemDecoration gives the current (centered) item horizontal margin so that
//            // it doesn't occupy the whole screen width. Without it the items overlap
//            val itemDecoration = HorizontalMarginItemDecoration(
//                it,null,
//                R.dimen.margin_10
//            )
//            viewPager.addItemDecoration(itemDecoration)
//        }
    }

    /** Method to set callbacks for viewpager page change , And sync both view pager t change position with respect to other  **/
    private fun setPageChangeCallback(currentViewPager: ViewPager2) {
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                AppLog.d(TAG, "onPageScrolled>>> $position  positionOffset   $positionOffset")

                /** For data container recalculate height of view pager as per data content wrapped height **/
//                if (currentViewPager == binding.vpDataContainer) {
//                    ViewUtils.recalculatePagerItemHeight(
//                        position,
//                        positionOffset,
//                        layoutManagerDataPager,
//                        binding.vpDataContainer
//                    )
//                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                AppLog.d(TAG, "onPageSelected >>> $position")
            }
        }

        currentViewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}