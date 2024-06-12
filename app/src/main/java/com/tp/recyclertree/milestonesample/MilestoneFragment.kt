package com.tp.recyclertree.milestonesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.FragmentMilestoneBinding
import com.tp.recyclertree.timelineSample.TimelineGraphAdapter
import com.tp.recyclertree.timelineSample.TimelineOfferDataAdapter

class MilestoneFragment : Fragment() {

    private lateinit var binding: FragmentMilestoneBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMilestoneBinding.inflate(inflater, container, false)
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

            /** Data item **/
            binding.rvMilestone.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

            /** Set sample view pager adapter **/
            val timelineOfferAdapter = TimelineOfferDataAdapter(
                listItems,
                userStateValue = 230,
                listOfferItemValues = listIOfferValues
            )

            binding.rvMilestone.adapter = timelineOfferAdapter
        }
    }

}