package com.example.seccouncil.payment_gateway.data.module

// Sealed class to represent different states of payment
sealed class PaymentState {
    object PaymentIdle : PaymentState() // Initial state, no payment happening
    object PaymentInProgress : PaymentState() // Payment is being processed
    data class PaymentSuccess(val paymentId: String) : PaymentState() // Payment was successful
    data class PaymentError(val code: Int, val message: String) : PaymentState() // Payment failed with an error
}
