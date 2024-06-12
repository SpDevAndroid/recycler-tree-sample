package com.tp.recyclertree.milestonesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

        binding.tvCta.setOnClickListener {
            if (binding.etUserAmount.text.toString().isNotEmpty()) {
                val amt = binding.etUserAmount.text.toString().toInt()
                setMilestoneAdapter(amt)
            }
        }

        setUpView()
    }

    private fun setUpView() {

        context?.let {
            /** Data item **/
            binding.rvMilestone.layoutManager = LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

            setMilestoneAdapter(300)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }


    private fun setMilestoneAdapter(userStateValue: Int) {

        val listIOfferValues = arrayListOf(
            MileStoneOffer(85),
            MileStoneOffer(300, 2),
            MileStoneOffer(700, 3),
            MileStoneOffer(900, 1)
        )


        /** Set sample view pager adapter **/
        val milestoneDataAdapter = MilestoneDataAdapter(
            listIOfferValues,
            userStateValue = userStateValue
        )

        binding.rvMilestone.adapter = milestoneDataAdapter

    }

}