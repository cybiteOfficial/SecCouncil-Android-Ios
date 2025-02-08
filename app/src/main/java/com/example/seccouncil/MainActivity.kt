package com.example.seccouncil

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.seccouncil.navigation.Navigation
import com.example.seccouncil.payment_gateway.PaymentDetails
import com.example.seccouncil.screens.SplashViewModel
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.ui.theme.SecCouncilTheme
import com.example.seccouncil.utlis.DataStoreManger
import com.example.seccouncil.utlis.preferenceDataStore
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject

class MainActivity : ComponentActivity(),PaymentResultWithDataListener {
    private var paymentDetails by mutableStateOf(PaymentDetails(null, null, 250000))
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition{viewModel.isLoading.value}
        setContent {
            Checkout.preload(applicationContext)
            val co = Checkout()
            co.setKeyID("rzp_test_CrYIryTQfdmHwu")
            co.setImage(R.drawable.app_icon)

            SecCouncilTheme {
                val dataStoreContext = LocalContext.current
                val dataStoreManger = DataStoreManger(dataStoreContext)
                Navigation(
                   preferenceDataStore =  preferenceDataStore,
                   dataStoreManger =  dataStoreManger,
                    context = dataStoreContext,
                    scope = lifecycleScope,
                    startRazorpayPayment = {startRazorpayPayment()}
                )
            }
        }
        /*
        * To ensure faster loading of the Checkout form,
        * call this method as early as possible in your checkout flow
        * */
    }
    private fun startRazorpayPayment() {
        Log.i("INSIDE","razorpay")
        // apart from setting it in AndroidManifest.xml, keyId can also be set
        // programmatically during runtime

        val activity: Activity = this
        val co = Checkout()

        try {
            val options = JSONObject()
            options.put("name","SecCouncil")
            options.put("description","Course Payment")
            //You can omit the image option to fetch the image from the Dashboard
            options.put("image","http://example.com/image/rzp.jpg")
            options.put("theme.color", "#38B6FF");
            options.put("currency","INR");
            //  options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("amount","50000")//pass amount in currency subunits

            val retryObj = JSONObject();
            retryObj.put("enabled", true);
            retryObj.put("max_count", 4);
            options.put("retry", retryObj);

            val prefill = JSONObject()
            prefill.put("email","gaurav.kumar@example.com")
            prefill.put("contact","9876543210")

            options.put("prefill",prefill)
            co.open(activity,options)
        }catch (e: Exception){
            Toast.makeText(activity,"Error in payment: "+ e.message,Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
    override fun onPaymentSuccess(paymentId: String?, paymentData: PaymentData?) {
        paymentDetails = paymentDetails.copy(paymentId = paymentId, orderId = paymentData?.orderId)
        Toast.makeText(this, "Payment Successful: $paymentId", Toast.LENGTH_LONG).show()
        // Handle payment success logic here
    }

    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        Toast.makeText(this, "Payment Failed: $response", Toast.LENGTH_LONG).show()
        // Handle payment error logic here
    }
}