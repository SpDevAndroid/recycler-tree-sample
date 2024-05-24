package com.tp.recyclertree.timelineSample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.FragmentTimelineBinding

private const val TAG = "TimelineFragment"

class TimelineFragment : Fragment() {

    private lateinit var binding: FragmentTimelineBinding

    private var adapter: TimelineGraphAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTimelineBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSampleViewPager()
        binding.tvCtaPocPager.setOnClickListener {
            findNavController().navigate(R.id.action_TimeLineFragment_to_ViewPagerFragment)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }


    private fun setSampleViewPager() {

        val listItems = arrayListOf(
            20, 40, 60, 80, 100,
            120, 140, 160, 180, 200,
            220, 240, 260, 280, 300,
            320, 340, 360, 380, 400
        )
        val listIOfferValues = arrayListOf(85, 300)

        context?.let {
            binding.rvGraph.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

            /** Set sample view pager adapter **/
            adapter = TimelineGraphAdapter(listItems)

            binding.rvGraph.adapter = adapter


            /** Data item **/
            binding.rvTimelineData.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

            /** Set sample view pager adapter **/
            val timelineOfferAdapter = TimelineOfferDataAdapter(listItems, userStateValue = 230, listOfferItemValues = listIOfferValues)

            binding.rvTimelineData.adapter = timelineOfferAdapter

        }
    }

}