package com.tp.recyclertree.viewpagersample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.FragmentViewPagerSampleBinding
import kotlin.math.abs


private const val TAG = "ViewPagerSampleFragment"

class ViewPagerSampleFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerSampleBinding

    private var adapter: ImageViewPager2Adapter? = null
    private var tabAdapter: TabViewPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentViewPagerSampleBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        /** Set sample view pager adapter **/
        adapter = ImageViewPager2Adapter(listItems)
        binding.vpSample.adapter = adapter
        setPageChangeCallback(binding.vpSample, binding.viewPagerTab)

        /** Set tab view pager adapter**/
        tabAdapter = TabViewPagerAdapter(listItems)
        binding.viewPagerTab.adapter = tabAdapter
        setItemDecorationTanViewPager(binding.viewPagerTab)
        setPageChangeCallback(binding.viewPagerTab, binding.vpSample)
    }

    private fun setItemDecorationTanViewPager(viewPager2: ViewPager2) {

        // You need to retain one page on each side so that the next and previous items are visible
        viewPager2.offscreenPageLimit = 1

        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position

            /** Item height scale changes not required , so commenting this code **/
            // Next line scales the item's height. You can remove it if you don't want this effect
//            page.scaleY = 1 - (0.25f * abs(position))

            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        viewPager2.setPageTransformer(pageTransformer)

        context?.let {
            // The ItemDecoration gives the current (centered) item horizontal margin so that
            // it doesn't occupy the whole screen width. Without it the items overlap
            val itemDecoration = HorizontalMarginItemDecoration(
                it,
                R.dimen.viewpager_current_item_horizontal_margin
            )
            viewPager2.addItemDecoration(itemDecoration)
        }
    }

    /** Method to set callbacks for viewpager page change , And sync both view pager t change position with respect to other  **/
    private fun setPageChangeCallback(currentViewPager: ViewPager2, targetViewPager: ViewPager2) {
        val onPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
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
                targetViewPager.currentItem = position
            }
        }

        currentViewPager.registerOnPageChangeCallback(onPageChangeCallback)
    }

}