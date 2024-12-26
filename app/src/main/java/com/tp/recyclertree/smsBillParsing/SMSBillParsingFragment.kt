package com.tp.recyclertree.smsBillParsing

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.Telephony
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.tp.recyclertree.AppLog
import com.tp.recyclertree.R
import com.tp.recyclertree.databinding.FragmentSmsBillParsingBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale


private const val TAG = "SMSBillParsingFragment"

class SMSBillParsingFragment : Fragment() {

    private lateinit var binding: FragmentSmsBillParsingBinding
    private val READ_SMS_PERMISSION_CODE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        this.binding = FragmentSmsBillParsingBinding.inflate(inflater, container, false)
        return this.binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRead.setOnClickListener {
            checkPermissionToReadSMS()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


    private fun checkPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_SMS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.READ_SMS),
                READ_SMS_PERMISSION_CODE
            )
        } else {
            readSms()
        }
    }

    private val smsList = ArrayList<String>()

    private fun readSms() {
        binding.tvSize.text = getString(R.string.filtering_your_credit_bill_messages)
        binding.rvSms.visibility = View.GONE
        val contentResolver = this.requireActivity().contentResolver


        val numberOfDaysInPast = 60L

        /** Get date of [numberOfDaysInPast] days before today**/
        val date30DaysBefore: Long =
            Date(System.currentTimeMillis() - numberOfDaysInPast * 24 * 3600 * 1000).time
        val arrayDateSelection = arrayOf("$date30DaysBefore")

        val projection = arrayOf(
            "_id", "address", "person",
            "body", "date", "type"
        )

        lifecycleScope.launch(Dispatchers.IO) {
            val cursor = contentResolver.query(
                Telephony.Sms.CONTENT_URI,
                projection,
                "date>?",
                arrayDateSelection,
                "date desc"
            )

            val nameColumn = cursor!!.getColumnIndex("person")
            val phoneNumberColumn = cursor!!.getColumnIndex("address")
            val smsbodyColumn = cursor!!.getColumnIndex("body")
            val dateColumn = cursor!!.getColumnIndex("date")
            val typeColumn = cursor!!.getColumnIndex("type")

            val listBankCodes = arrayListOf("ICICIT", "ICICIB", "HDFCBK", "HDFCBN")
            val listCardLastDigits = arrayListOf("XX5018", "XX1407")
            val listKeyWords = arrayListOf("statement", "bill")

            val finalFilteredListMessages = ArrayList<BillData>()
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    val address =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    smsList.add("Sender: $address\nMessage: $body")

//               val name = (cursor.getString(nameColumn))
//                val dateColumn = (cursor.getString(dateColumn))
//                val phoneNumberColumn = (cursor.getString(phoneNumberColumn))
//                val smsbodyColumn  = (cursor.getString(smsbodyColumn))
//                val typeColumn  = (cursor.getString(typeColumn))

                    AppLog.d(TAG, " address $address")
                    val bill = BillParseUtil.parseCreditCardSMS(body)
                    bill?.let {
                        AppLog.d(TAG, "CreditBillParsing address : $address bill : $bill")
                        bill.completeMessage = body
                        bill.senderId = address
                        finalFilteredListMessages.add(bill)
                    }
//                    if (isValidBank(address, listBankCodes) && isKeyWordPresent(
//                            body,
//                            listKeyWords
//                        ) && isCardLastDigitsPresent(body, listCardLastDigits)
//                    ) {
//                        AppLog.d(TAG, "isValidSMS = true")
//                        finalFilteredListMessages.add(body)
//                    }

//                AppLog.d("mvv12"," name $name  dateColumn  $dateColumn   phoneNumberColumn   $phoneNumberColumn   smsbodyColumn  $smsbodyColumn   typeColumn  $typeColumn   ")


                } while (cursor.moveToNext())

            }




            AppLog.d(TAG, " finalFilteredListMessages size : ${finalFilteredListMessages.size}")
            AppLog.d(TAG, "smsList size >>>>>  ${smsList.size}  ")

            launch(Dispatchers.Main) {
                val showStr =
                    "Total SMS List size :  ${smsList.size} \n\nFinal Filtered SMS List size : ${finalFilteredListMessages.size}"
                binding.tvSize.text = showStr
                setFilteredSMSAdapter(finalFilteredListMessages)
            }

            cursor.close()
        }
    }

    private fun setFilteredSMSAdapter(billDataList: ArrayList<BillData>) {
        binding.rvSms.visibility = View.VISIBLE
        activity?.let {
            binding.rvSms.layoutManager =
                LinearLayoutManager(it, LinearLayoutManager.VERTICAL, false)
            val adapter = SMSListAdapter(billDataList)
            binding.rvSms.adapter = adapter
        }
    }

    private fun isValidBank(smsSenderStr: String, listBankCodes: ArrayList<String>): Boolean {
        for (bankCode in listBankCodes) {
            if (smsSenderStr.lowercase().contains(bankCode.lowercase())) {
                return true
            }
        }
        return false
    }

    private fun isKeyWordPresent(smsBodyStr: String, listKeywords: ArrayList<String>): Boolean {
        for (keyword in listKeywords) {
            if (smsBodyStr.lowercase().contains(keyword.lowercase())) {
                return true
            }
        }
        return false
    }

    private fun isCardLastDigitsPresent(
        smsBodyStr: String,
        listCardLastDigits: ArrayList<String>
    ): Boolean {
        for (cardNumberLastDigits in listCardLastDigits) {
            if (smsBodyStr.lowercase().contains(cardNumberLastDigits.lowercase())) {
                return true
            }
        }
        return false
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_SMS_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                readSms()
//                val adapter = listView.getAdapter() as ArrayAdapter<String>
//                adapter.notifyDataSetChanged()
            }
        }
    }
}