package com.tp.recyclertree.smsBillParsing

class CreditCardBill {
    // Getters and Setters
    var card: String? = null
    var billAmount: String? = null
    var minBillAmount: String? = null
    var dueDate: String? = null

    override fun toString(): String {
        return "CreditCardBill{" +
                "card='" + card + '\'' +
                "billAmount='" + billAmount + '\'' +
                "minAmount='" + minBillAmount + '\'' +
                ", dueDate='" + dueDate + '\'' +
                '}'
    }
}
