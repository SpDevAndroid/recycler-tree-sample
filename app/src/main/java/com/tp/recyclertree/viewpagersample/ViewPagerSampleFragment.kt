package com.tp.recyclertree.viewpagersample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.FragmentViewPagerSampleBinding
import com.tp.recyclertree.databinding.ViewPagerTabItemBinding


private const val TAG = "ViewPagerSampleFragment"

class ViewPagerSampleFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerSampleBinding

    private var adapter: ImageViewPager2Adapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentViewPagerSampleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_ViewPagerFragment_to_FirstFragment)
//        }

        setSampleViewPager()
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }

    private fun setSampleViewPager() {


        val listItems = arrayListOf(
            R.drawable.img_view_pager_sample_1,
            R.drawable.img_view_pager_sample_2,
            R.drawable.img_view_pager_sample_1,
            R.drawable.img_view_pager_sample_2
        )
        adapter = ImageViewPager2Adapter(listItems)

        /** Set tabs for viewpager in both visible and collapsed state tab layout **/
        setUpTabsForViewPager(binding.tlViewPagerSample)


        binding.vpSample.registerOnPageChangeCallback(onPageChangeCallback)
    }


    private fun setUpTabsForViewPager(tabLayout: TabLayout) {
        tabLayout.setOnTouchListener { _v, _event -> true }
        binding.vpSample.adapter = adapter
        TabLayoutMediator(
            tabLayout,
            binding.vpSample
        ) { tab, position ->
            val bindingTab = ViewPagerTabItemBinding.inflate(layoutInflater, tab.view, false)
            tab.customView = bindingTab.root
            val tabText = "TAB $position"
            bindingTab.tvTitle.text = tabText

        }.attach()

        /** Set tabs margin after tabs value is set up **/
        setTabsMargin(tabLayout)
    }

    /** Method to set up tab margin
     * @Note Call this method after tabs has been added
     * **/
    private fun setTabsMargin(tabLayout: TabLayout) {
        for (i in 0 until tabLayout.tabCount) {
            val tab = (tabLayout.getChildAt(0) as ViewGroup).getChildAt(i)
            val p = tab.layoutParams as ViewGroup.MarginLayoutParams
            val marginEnd = resources.getDimension(R.dimen.margin_4)
            var marginStart = 0

            /** Add Extra margin to start of first item only , Design team requirement : TIMPR-16160 **/
            if (i == 0) {
                marginStart = resources.getDimension(R.dimen.margin_3).toInt()
            }
            p.setMargins(marginStart, 0, marginEnd.toInt(), 0)
            tab.requestLayout()
        }
    }

    private val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            AppLog.d(TAG, "onPageScrolled>>> $position  positionOffset   $positionOffset")

        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            AppLog.d(TAG, "onPageSelected >>> $position")
        }
    }
}