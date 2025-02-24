package com.example.seccouncil.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seccouncil.payment_gateway.PaymentViewModel
import com.example.seccouncil.screens.Login
import com.example.seccouncil.screens.Otp
import com.example.seccouncil.screens.SignUp
import com.example.seccouncil.screens.SignUpViewModel
import com.example.seccouncil.screens.SignUpViewModelFactory
import com.example.seccouncil.screens.homescreen.HomeScreen
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.screens.homescreen.MyViewModelFactory
import com.example.seccouncil.utlis.DataStoreManger
import com.example.seccouncil.utlis.DataStoreManger.Companion.EMAIL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    // NavController to manage app navigation, with a default value using rememberNavController
    navController: NavHostController = rememberNavController(),
    // DataStore instance for storing preferences
    preferenceDataStore: DataStore<Preferences>,
    // Our custom DataStore manager
    dataStoreManger: DataStoreManger,
    context: Context,
    scope: CoroutineScope,
    paymentViewModel: PaymentViewModel
//    startRazorpayPayment:()->Unit
) {
    // Initialize ViewModel using factory pattern
    // This ViewModel handles sign-up related logic
    val authViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(
            dataStoreManager = dataStoreManger,
            // Callback for successful registration - navigates to Home screen
            onRegisterSuccess = {
                navController.navigate(Routes.Login.name) {
                    // Remove SignUp screen from back stack when navigating
                    popUpTo(Routes.Signup.name) { inclusive = true }
                }
            }
        )
    )

    val homeViewmodel:HomescreenViewmodel = viewModel(
        factory = MyViewModelFactory(
            dataStoreManager = dataStoreManger,
            context = context
        )
    )

    // State to track if user is registered
    var isRegistered by remember {
        mutableStateOf(false)
    }

    // Create a coroutine scope that remembers across recompositions
    val scope = rememberCoroutineScope()

    // Logout function that clears user data
    val onLogout = {
        isRegistered = false
        scope.launch {
            dataStoreManger.clearDataStore()
        }
    }

//    // Variable to store the initial screen to show
//    var startDestination by remember { mutableStateOf(Routes.Signup.name) }
//
//    // Effect that runs when the app starts
//    // Checks if user is registered and sets appropriate start screen
//    LaunchedEffect(Unit) {
//        checkRegisterState(preferenceDataStore) { isUserRegistered ->
//            isRegistered = isUserRegistered
//            startDestination = if (isUserRegistered) Routes.Home.name else Routes.Signup.name
//        }
//    }
    // Use produceState to get registration state before NavHost initializes
    val startDestination by produceState(initialValue = Routes.Signup.name) {
        checkRegisterState(preferenceDataStore) { isUserRegistered ->
            value = if (isUserRegistered) Routes.Home.name else Routes.Signup.name
        }
    }

    // NavHost defines the navigation graph
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
    ) {
        // Define the SignUp screen route
        composable(route = Routes.Signup.name) {
            SignUp(
                // Navigation callback to OTP screen
                onClickToOTP = { navController.navigate(Routes.Otp.name) },
                authViewModel = authViewModel,
                // Navigation callback to Login screen
                onLoginClicked = {
                    navController.navigate(Routes.Login.name) {
                        // Remove SignUp screen from back stack
                        popUpTo(Routes.Signup.name) { inclusive = true }
                    }
                },
            )
        }

        // Define the Login screen route
        composable(route = Routes.Login.name) {
            Login(
                // Navigation callback after successful login
                onLoginClicked = {
                    navController.navigate(Routes.Home.name) {
                        popUpTo(Routes.Login.name) { inclusive = true }
                    }
                },
                onRegisterClick = { navController.navigate(Routes.Signup.name) }
            )
        }

        // Define the OTP verification screen route
        composable(route = Routes.Otp.name) {
            Otp(
                onVerifyClicked = {
                    navController.navigate(Routes.Login.name)
                },
                authViewModel = authViewModel
            )
        }

        // Define the Home screen route
        composable(route = Routes.Home.name) {
            HomeScreen(
                userDetails = dataStoreManger.getFromDataStore(),
                profileViewmodel = homeViewmodel,
                scope = scope
//                startRazorpayPayment = startRazorpayPayment
                ,
                paymentViewModel = paymentViewModel
            )
        }
    }
}

// Function to check if user is registered by looking for email in DataStore
suspend fun checkRegisterState(
    preferenceDataStore: DataStore<Preferences>,
    // Callback function that receives the result
    onResult: (Boolean) -> Unit
) {

    // Get first emission from DataStore preferences
    // "DataStore.data returns a Flow (stream) of preferences. Using .first() is like checking " +
    // "your mailbox once: you grab what's there and leave. It gets the current state of preferences
    // without watching for future changes."
    val preferences = preferenceDataStore.data.first()
    // Check if email exists in preferences
    val email = preferences[EMAIL]
    // User is registered if email is not null
    val isRegistered = email != null
    // Call the callback with the result
    onResult(isRegistered)
}