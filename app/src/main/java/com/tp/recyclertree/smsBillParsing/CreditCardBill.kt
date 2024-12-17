package com.tp.recyclertree.smsBillParsing

class CreditCardBill {
    // Getters and Setters
    var card: String? = null
    var billAmount: String? = null
    var minBillAmount: String? = null
    var dueDate: String? = null
    var lastPaymentDate: String? = null
    var availableCredit: String? = null
    var minPayment: String? = null

    override fun toString(): String {
        return "CreditCardBill{" +
                "card='" + card + '\'' +
                "billAmount='" + billAmount + '\'' +
                "minAmount='" + minBillAmount + '\'' +
                ", dueDate='" + dueDate + '\'' +
                ", lastPaymentDate='" + lastPaymentDate + '\'' +
                ", availableCredit='" + availableCredit + '\'' +
                ", minPayment='" + minPayment + '\'' +
                '}'
    }
}
