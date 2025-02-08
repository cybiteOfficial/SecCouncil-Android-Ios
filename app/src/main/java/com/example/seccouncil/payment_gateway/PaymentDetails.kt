package com.example.seccouncil.payment_gateway

data class PaymentDetails(
    val orderId: String?,
    val paymentId: String?,
    val amount: Int
)
