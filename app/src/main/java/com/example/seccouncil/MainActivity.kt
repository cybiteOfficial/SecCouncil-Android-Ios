package com.example.seccouncil

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.seccouncil.navigation.Navigation
import com.example.seccouncil.payment_gateway.PaymentDetails
import com.example.seccouncil.payment_gateway.PaymentViewModel
import com.example.seccouncil.screens.SplashViewModel
import com.example.seccouncil.ui.theme.SecCouncilTheme
import com.example.seccouncil.utlis.DataStoreManger
import com.example.seccouncil.utlis.preferenceDataStore
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(),PaymentResultWithDataListener {
    private var paymentDetails by mutableStateOf(PaymentDetails(null, null, 250000))
    private val viewModel: SplashViewModel by viewModels()
    private val paymentViewModel:PaymentViewModel by viewModels()



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition{viewModel.isLoading.value}
        setContent {
            Checkout.preload(applicationContext)

            SecCouncilTheme {
                val dataStoreContext = LocalContext.current
                val dataStoreManger = DataStoreManger(dataStoreContext)
                Navigation(
                   preferenceDataStore =  preferenceDataStore,
                   dataStoreManger =  dataStoreManger,
                    context = dataStoreContext,
                    scope = lifecycleScope,
                    paymentViewModel = paymentViewModel
                )
            }
        }
        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */
    }
    override fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {
        Log.e("Payment_status","$paymentId, ${paymentData?.orderId}")


        if (paymentId != null) {
            paymentViewModel.handlePaymentSuccess(paymentId)

            // ✅ Get stored course ID
            val courseId = paymentViewModel.selectedCourseId.value ?: ""

            if (courseId.isNotEmpty()) {
                Log.e("CH1","$courseId")
                // ✅ Call API to verify payment and enroll user
                lifecycleScope.launch {
                    val verifyResponse = paymentData?.let {
                        paymentViewModel.verifyUserPayment(
                            orderId = it.orderId,
                            paymentId = paymentId,
                            signature = it.signature,
                            courses = listOf(courseId),
                            context = applicationContext
                        )
                    }
                    Log.e("CH1","$verifyResponse")
                    if (verifyResponse != null) {
                        Toast.makeText(this@MainActivity, "Payment Successful! Course Enrolled.", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Payment Verified, but Enrollment Failed!", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this@MainActivity, "Error: Course ID Missing!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        Log.e("Payment Error", "Code: $code, Response: $response, OrderId: ${paymentData?.orderId}")
        paymentViewModel.handlePaymentError(code, response ?: "Unknown error")
    }
}