package com.example.seccouncil.payment_gateway

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.seccouncil.R
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.screens.homescreen.ResponsiveCourseDetailScreen
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class PaymentActivity: ComponentActivity(), PaymentResultWithDataListener {

    private var paymentDetails by mutableStateOf(PaymentDetails(null, null, 250000))
    // Initialize ViewModel
    private val profileViewmodel: HomescreenViewmodel by viewModels()
    // .....
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Log.i("INSIDE","Payment activity")
            // Retrieve courseId from intent (if passed)
            val courseId = intent.getStringExtra("COURSE_ID") ?: ""
            ResponsiveCourseDetailScreen(
                courseId = courseId,
                paymentDetails = paymentDetails,
                onBuy = {startRazorpayPayment()},
                profileViewmodel = profileViewmodel
            )
        }
    }

 fun startRazorpayPayment() {
     Log.i("INSIDE","razorpay")

        Checkout.preload(applicationContext)
        val co = Checkout()
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime
        co.setKeyID("rzp_test_CrYIryTQfdmHwu")

        co.setImage(R.drawable.app_icon)
     try {
         val options = JSONObject().apply {
             put("name", "SecCouncil")
             put("description", "Course Payment")
             put("theme.color", "#38B6FF")
             put("prefill.email", "niteshkrjhag@gmail.com")
             put("prefill.contact", "7673009568")
             put("currency", "INR")
             // Amount is in paise, e.g., 2500000 paise = ₹25000
             // Replace paymentDetails.amount with your intended amount
             put("amount", 2500000)

             // You can add more options as per your needs:
             // put("order_id", "order_DBJOWzybf0sJbty") // If you have an order ID from your server
         }

         // Example of a retry mechanism in Razorpay (optional):
         val retryObject = JSONObject()
         retryObject.put("enabled", true)
         retryObject.put("max_count", 4)
         options.put("retry", retryObject)


         // Start the payment flow
            co.open(this, options)
        } catch (e: Exception) {
            Toast.makeText(this, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    /*
    * To ensure faster loading of the Checkout form,
    * call this method as early as possible in your checkout flow
    * */


override fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {
    paymentDetails = paymentDetails.copy(paymentId = paymentId, orderId = paymentData?.orderId)
    Toast.makeText(this, "Payment Successful", Toast.LENGTH_LONG).show()
    // Handle payment success logic here
}

override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
    Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show()
    // Handle payment error logic here
}
    //......
}