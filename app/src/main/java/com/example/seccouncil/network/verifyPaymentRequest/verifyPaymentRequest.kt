package com.example.seccouncil.network.verifyPaymentRequest

data class verifyPaymentRequest(
    val courses: List<String>,
    val razorpay_order_id: String,
    val razorpay_payment_id: String,
    val razorpay_signature: String
)