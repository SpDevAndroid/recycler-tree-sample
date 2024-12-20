package com.tp.recyclertree.smsBillParsing

import com.tp.recyclertree.AppLog
import java.util.regex.Matcher
import java.util.regex.Pattern


private const val TAG = "CreditBillParsing"

object BillParseUtil {

    fun parseCreditCardSMS(smsText: String): BillData? {
//        AppLog.d(TAG, "parseCreditCardSMS smsText : $smsText")
        val bill = BillData()

        // Regular expression patterns to extract relevant details
        val cardPattern = arrayListOf(
            Pattern.compile("Card(?:.?)(\\+\\d{4}|XX\\d{4})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Card no.(?:.?)(\\+\\d{4}|XX\\d{4})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Card ending(?:.?)(\\+\\d{4}|XX\\d{4})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("card ending with (?:.?)(\\+\\d{4}|XX\\d{4})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Card\\s*(XX\\d{4})", Pattern.CASE_INSENSITIVE), // HDFC, ICICI
        )

        val amountPattern = arrayListOf(
            Pattern.compile("Total due amt: Rs.\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // HDFC BANK TESTED, working fine
            Pattern.compile("Total of Rs\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // ICICI TESTED
            Pattern.compile("Total due amt:\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // HDFC
            Pattern.compile("Total amount due: INR Dr.\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // Axis Bank
            Pattern.compile("Total payment of Rs.\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // AMEX
            Pattern.compile("Total Amt Due Rs\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // SBI
            Pattern.compile("INR\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE), // HSBC

            Pattern.compile("Bill Amount:\\s*([\\d,]+\\.\\d{2})", Pattern.CASE_INSENSITIVE),
            Pattern.compile("Total(?: amount)?(?: due)?:?\\s*(?:Rs\\.|INR|Dr\\.?)?\\s*([\\d,.]+)" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("\\$([\\d,\\.]+)" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Amount: \\$(\\d+\\.\\d{2})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("INR\\s*(\\d+(?:\\.\\d{1,2})?)" , Pattern.CASE_INSENSITIVE),

            // Electricity bill samples
            Pattern.compile("Rs\\.\\s*(\\d+\\.\\d{2})" , Pattern.CASE_INSENSITIVE), //
            Pattern.compile("Rs\\.*\\s*([\\d,]+\\.\\d{2})" , Pattern.CASE_INSENSITIVE), // BSES
            Pattern.compile("Rs\\.*\\s*(\\d+/-)" , Pattern.CASE_INSENSITIVE), // AVVNL
        )

        val minAmountPattern = arrayListOf(
            Pattern.compile("Minimum(?: amt)?(?: due)?:?\\s*(?:Rs\\.|INR|Dr\\.?)?\\s*([\\d,.]+)" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Minimum Payment:\\s*([\\d,]+\\.\\d{2})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("minimum of Rs\\s*([\\d,]+\\.*\\d{0,2})", Pattern.CASE_INSENSITIVE), // ICICI TESTED
            Pattern.compile("Min due amt: Rs.\\s*([\\d,]+\\.*\\d{0,2})", Pattern.CASE_INSENSITIVE), // HDFC
            Pattern.compile("Min Amt Due Rs\\s*([\\d,]+\\.*\\d{0,2})", Pattern.CASE_INSENSITIVE), // HDFC
            Pattern.compile("Minimum amt due: INR Dr.\\s*([\\d,]+\\.*\\d{0,2})", Pattern.CASE_INSENSITIVE), // Axis Bank
            Pattern.compile("minimum payment due is INR\\s*([\\d,]+\\.*\\d{0,2})", Pattern.CASE_INSENSITIVE), // Axis Bank

        )

        val dueDatePattern = arrayListOf(
            Pattern.compile("Due date:\\s*(\\d{1,2}/\\d{1,2}/\\d{4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Due by:\\s*(\\d{1,2}/\\d{1,2}/\\d{4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Due(?: by| date)?:?\\s*(\\d{2}[-/][A-Z]{3}[-/]\\d{2,4}|\\d{2}[-/][A-Za-z]+[-/]\\d{2,4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("(\\d{1,2}[a-z]{2}\\s+[a-zA-Z]+\\s+\\d{4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Date:\\s*(\\d{4}-\\d{2}-\\d{2})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Due date:\\s*(\\d{2,4}-\\d{2}-\\d{2,4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Due by:\\s*(\\d{2,4}-\\d{2}-\\d{2,4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("Due(?: by| date)?:?\\s*(\\d{2,4}-\\d{2}-\\d{2,4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("(?:due date|by)\\s*([\\d]{1,2}[a-zA-Z]+(?:\\s*\\w+)?)" , Pattern.CASE_INSENSITIVE),

            // Electricity bill samples
            Pattern.compile("Due date:\\s*(\\d{1,2}[a-zA-Z]{2}\\s*[a-zA-Z]+\\s*\\d{4})" , Pattern.CASE_INSENSITIVE),
            Pattern.compile("due on\\s*(\\d{1,2}\\.\\d{1,2}\\.\\d{4})" , Pattern.CASE_INSENSITIVE), // BSES
            Pattern.compile("due on\\s*(\\d{1,2}-[a-zA-Z]{3}-\\d{4})" , Pattern.CASE_INSENSITIVE) // AVVNL
        )

        val consumptionUnitPattern = arrayListOf(
            // Electricity bill samples
            Pattern.compile("Total consumption:\\s*(\\d+)\\s*kWh", Pattern.CASE_INSENSITIVE),
        )

        val billNumberPatterns = arrayListOf(
            // Electricity bill samples
            Pattern.compile("CA\\s*(\\d+),*", Pattern.CASE_INSENSITIVE), // BSES
            Pattern.compile("KNO\\s*(\\d+)\\s", Pattern.CASE_INSENSITIVE), // AVVNL
        )

        var matcher: Matcher
        // Extract details using regex matching
        for (cardPatternItem in cardPattern) {
            matcher = cardPatternItem.matcher(smsText)
            if (matcher.find()) {
                bill.card = matcher.group(1)
                break
            }
        }

        for (amountPatternItem in amountPattern) {
            matcher = amountPatternItem.matcher(smsText)
            if (matcher.find()) {
                bill.billAmount = matcher.group(1)
                break
            }
        }

        for (minAmountPatternItem in minAmountPattern) {
            matcher = minAmountPatternItem.matcher(smsText)
            if (matcher.find()) {
                bill.minBillAmount = matcher.group(1)
                break
            }
        }

        for (dueDatePatternItem in dueDatePattern) {
            matcher = dueDatePatternItem.matcher(smsText)
            if (matcher.find()) {
                bill.dueDate = matcher.group(1)
                break
            }
        }

        for (consumptionUnitPatternItem in consumptionUnitPattern) {
            matcher = consumptionUnitPatternItem.matcher(smsText)
            if (matcher.find()) {
                bill.consumptionUnits = matcher.group(1)
                break
            }
        }

        for (billNumberPatternItem in billNumberPatterns) {
            matcher = billNumberPatternItem.matcher(smsText)
            if (matcher.find()) {
                bill.billNumber = matcher.group(1)
                break
            }
        }


        if (bill.billNumber?.isNotEmpty() == true) {
            AppLog.d(TAG, "parseCreditCardSMS : billNumber  : ${bill.billNumber}")
        }
        if (bill.consumptionUnits?.isNotEmpty() == true) {
            AppLog.d(TAG, "parseCreditCardSMS : billAmount  : ${bill.billAmount} consumptionUnits  : ${bill.consumptionUnits}")
        }
        if (bill.dueDate?.isNotEmpty() == true) {
            AppLog.d(TAG, "parseCreditCardSMS card  : ${bill.card} : dueDate  : ${bill.dueDate} billAmount  : ${bill.billAmount} sms : $smsText")
        }
        return if ((bill.billAmount?.isNotEmpty() == true) && (bill.dueDate?.isNotEmpty() == true)) bill else null
    }
}