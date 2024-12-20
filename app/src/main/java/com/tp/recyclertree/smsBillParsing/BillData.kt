package com.tp.recyclertree.smsBillParsing

class BillData {
    // Getters and Setters
    var card: String? = null
    var billAmount: String? = null
    var minBillAmount: String? = null
    var dueDate: String? = null
    var completeMessage: String? = null
    var senderId: String? = null
    var consumptionUnits: String? = null
    var billNumber: String? = null

    override fun toString(): String {
        return "BillData{" +
                "card='" + card + '\'' +
                "billAmount='" + billAmount + '\'' +
                "minAmount='" + minBillAmount + '\'' +
                ", dueDate='" + dueDate + '\'' +
                '}'
    }
}
