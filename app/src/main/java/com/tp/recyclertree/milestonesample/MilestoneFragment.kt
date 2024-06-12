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
            findNavController().navigate(R.id.action_MileStoneFragment_to_ViewPagerFragment)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }


    private fun setSampleViewPager() {

        val listIOfferValues = arrayListOf(85, 300, 500, 800)

        context?.let {

            /** Data item **/
            binding.rvMilestone.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

            /** Set sample view pager adapter **/
            val milestoneDataAdapter = MilestoneDataAdapter(
                listIOfferValues,
                userStateValue = 200
            )

            binding.rvMilestone.adapter = milestoneDataAdapter
        }
    }

}