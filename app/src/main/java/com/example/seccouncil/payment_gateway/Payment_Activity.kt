package com.example.seccouncil.payment_gateway

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seccouncil.network.ApiService
import com.example.seccouncil.network.CapturePaymentRequest
import com.example.seccouncil.network.capturePayment.capturePaymentResponseX
import com.example.seccouncil.network.verifyPaymentRequest.verifyPaymentRequest
import com.example.seccouncil.network.verifyPaymentResponse
import com.example.seccouncil.payment_gateway.data.config.RazorpayConfig
import com.example.seccouncil.payment_gateway.data.module.PaymentState
import com.example.seccouncil.utlis.DataStoreManger
import com.razorpay.Checkout
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class PaymentViewModel : ViewModel() {

    // StateFlow that holds the current state of the payment process
    private val _paymentState = MutableStateFlow<PaymentState>(PaymentState.PaymentIdle)
    val paymentState = _paymentState.asStateFlow() // Read-only state flow to be accessed by the UI

    // Function to start the Razorpay payment process
    fun startRazorpayPayment(
        coursePrice: Int, // Price of the course to be paid
        email: String = "", // User's email (optional)
        phone: String = "", // User's phone number (optional)
        activity: Activity, // The activity context to open the payment gateway
        orderId:String
    ) {
        // Update the state to show that payment is in progress (only if it's idle)
        if (_paymentState.value is PaymentState.PaymentIdle) {
            _paymentState.value = PaymentState.PaymentInProgress
        }

        try {
            // Create a JSON object to configure Razorpay payment options
            val options = JSONObject()
            options.put("name", "SecCouncil") // Name of the company
            options.put("description", "Course Payment") // Description of the transaction
            options.put("image", "http://example.com/image/rzp.jpg") // Image for the payment popup (can be omitted)
            options.put("theme.color", "#38B6FF") // Set theme color
            options.put("currency", "INR") // Set the currency to INR
            options.put("order_id", orderId) // ✅ Passing the orderId to Razorpay

            // options.put("order",orderId)

            // Configure UPI options (flow intent to open UPI apps)
            options.put("upi", JSONObject().apply {
                put("flow", "intent")
            })

            // Convert course price to smallest currency unit (paisa) for Razorpay
            val amountInPaisa = coursePrice * 100
            options.put("amount", amountInPaisa) // Amount in paisa (100 paise = 1 INR)

            // Retry configuration (max 4 retries if payment fails)
            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            retryObj.put("max_count", 4)
            options.put("retry", retryObj)

            // Prefill user details like email and phone for faster checkout
            val prefill = JSONObject()
            prefill.put("email", email)
            prefill.put("contact", phone)
            options.put("prefill", prefill)

            // Initialize Razorpay checkout and open the payment gateway
            val checkout = Checkout()
            checkout.setKeyID(RazorpayConfig.KEY_ID) // Set your Razorpay API key
            checkout.open(activity, options) // Open Razorpay payment UI

            Log.e("Payment_amount", amountInPaisa.toString())
            Log.e("Razorpay Order ID", orderId) // ✅ Log Order ID

        } catch (e: Exception) {
            // Handle any errors and show a message to the user
            Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()

            // Set the state to PaymentError if something goes wrong
            if (_paymentState.value !is PaymentState.PaymentError) {
                _paymentState.value = PaymentState.PaymentError(0, e.message.toString())
            }
        }
    }

    // Handle the case where payment is successful
    fun handlePaymentSuccess(paymentId: String) {
        // Set state to PaymentSuccess with the payment ID
        if (_paymentState.value !is PaymentState.PaymentSuccess) {
            _paymentState.value = PaymentState.PaymentSuccess(paymentId)
        }
    }

    // Handle the case where payment failed
    fun handlePaymentError(code: Int, message: String) {
        // Set state to PaymentError with the error code and message
        if (_paymentState.value !is PaymentState.PaymentError) {
            _paymentState.value = PaymentState.PaymentError(code, message)

            // Reset state after a delay so that user can retry
            viewModelScope.launch {
                delay(2000)  // Wait for 2 seconds before resetting
                _paymentState.value = PaymentState.PaymentIdle
            }
        }
    }

    suspend fun captureUserPayment(context: Context, courses: List<String>): capturePaymentResponseX? {
        val apiService = ApiService.createAuthenticatedApi(context)
        val token = runBlocking { DataStoreManger(context).getStoredToken().firstOrNull() }

        if (token.isNullOrEmpty()) {
            Log.e("Payment", "Token is missing")
            return null
        }

        val request = CapturePaymentRequest(courses)
        val response = apiService.capturePayment("Bearer $token", request)

        return if (response.isSuccessful && response.body() != null) {
            val paymentResponse = response.body()
            Log.i("Payment", "Payment Created: ${paymentResponse?.data?.id}")
            Log.i("Payment", "Amount Due: ${paymentResponse?.data?.amount_due} ${paymentResponse?.data?.currency}")
            paymentResponse // Return the response instead of Unit
        } else {
            Log.e("Payment", "Failed: ${response.errorBody()?.string()}")
            null
        }
    }
    fun resetPaymentState() {
        _paymentState.value = PaymentState.PaymentIdle
    }

    private val _selectedCourseId = MutableStateFlow<String>("")
    val selectedCourseId: StateFlow<String> get() = _selectedCourseId

    fun setCourseId(courseId: String) {
        _selectedCourseId.value = courseId
    }

    suspend fun verifyUserPayment(
        context: Context,
        orderId: String,
        paymentId: String,
        signature: String,
        courses: List<String>
    ): verifyPaymentResponse? {
        return try {
            val apiService = ApiService.createAuthenticatedApi(context)
            val token = runBlocking { DataStoreManger(context).getStoredToken().firstOrNull() }

            if (token.isNullOrEmpty()) {
                Log.e("Payment", "Token is missing")
                return null
            }

            val requestBody = verifyPaymentRequest(
                razorpay_order_id =  orderId,
                razorpay_payment_id = paymentId,
                razorpay_signature =signature,
                courses= courses
            )

            val response = apiService.verifyPayment(
                authHeader = "Bearer $token",
                request = requestBody
            )

            if (response.isSuccessful) {
                response.body()
            } else {
                Log.e("PaymentVerification", "Verification failed: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("PaymentVerification", "Error verifying payment: ${e.message}")
            null
        }
    }
}
