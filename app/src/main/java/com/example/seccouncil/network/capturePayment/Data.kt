package com.example.seccouncil.network.capturePayment

data class Data(
    val amount: Int,
    val amount_due: Int,
    val amount_paid: Int,
    val attempts: Int,
    val created_at: Int,
    val currency: String,
    val entity: String,
    val id: String,
    val notes: List<Any>,
    val offer_id: Any,
    val receipt: String,
    val status: String
)