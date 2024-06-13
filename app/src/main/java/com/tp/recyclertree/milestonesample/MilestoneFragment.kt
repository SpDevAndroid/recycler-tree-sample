package com.tp.recyclertree.milestonesample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.databinding.FragmentMilestoneBinding

private const val TAG = "MilestoneFragment"

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
            binding.rvMilestone.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.HORIZONTAL, false)

            setMilestoneAdapter(300)
        }
    }

    override fun onDestroyView() {

        super.onDestroyView()
    }


    private fun setMilestoneAdapter(userStateValue: Int) {

        val listIOfferValues = arrayListOf(
            MileStoneOffer(85, 2),
            MileStoneOffer(300, 2),
            MileStoneOffer(700, 3),
            MileStoneOffer(900, 1)
        )


        /** Prepare list to add space units between offer items  **/
        val finalList = ArrayList<MileStoneOffer>()
        for (indexOffer in 0 until listIOfferValues.size) {
            val offerItem = listIOfferValues[indexOffer]
            var prevVal = 0
            if ((indexOffer - 1) >= 0) {
                prevVal = listIOfferValues[(indexOffer - 1)].amt
            }
            AppLog.d(
                TAG,
                "setMilestoneAdapter offerItem.amt : ${offerItem.amt} prevVal : $prevVal offerItem.unitSpace : ${offerItem.unitSpace}"
            )
            if (offerItem.unitSpace > 1) {
                val amtUnit = (offerItem.amt - prevVal) / (offerItem.unitSpace + 1)
                AppLog.d(TAG, "setMilestoneAdapter amtUnit : $amtUnit")
                for (i in 0 until offerItem.unitSpace) {
                    val spaceAmt = prevVal + (amtUnit * (i + 1))
                    val spaceOffer =
                        MileStoneOffer(spaceAmt, viewType = MileStoneOffer.ItemType.EMPTY_SPACE)

                    AppLog.d(TAG, "setMilestoneAdapter spaceOffer : $spaceOffer")

                    finalList.add(spaceOffer)
                }
                finalList.add(offerItem)
            } else {
                finalList.add(offerItem)
            }

        }


        AppLog.d(TAG, "setMilestoneAdapter finalList : $finalList")

        /** Set sample view pager adapter **/
        val milestoneDataAdapter = MilestoneDataAdapter(
            finalList,
            userStateValue = userStateValue
        )

        binding.rvMilestone.adapter = milestoneDataAdapter

    }

}