package com.example.seccouncil.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.example.seccouncil.model.UserDetails
import com.example.seccouncil.screens.Login
import com.example.seccouncil.screens.Otp
import com.example.seccouncil.screens.SignUp
import com.example.seccouncil.screens.SignUpViewModel
import com.example.seccouncil.screens.SignUpViewModelFactory
import com.example.seccouncil.screens.homescreen.HomeScreen
import com.example.seccouncil.screens.profilesetting.ProfileSettingScreen
import com.example.seccouncil.utlis.DataStoreManger
import com.example.seccouncil.utlis.DataStoreManger.Companion.EMAIL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun Navigation(
    navController:NavHostController = rememberNavController(),
    preferenceDataStore: DataStore<Preferences>,
    dataStoreManger: DataStoreManger
){

    val authViewModel: SignUpViewModel = viewModel(
        factory = SignUpViewModelFactory(
            dataStoreManager = dataStoreManger,
            onRegisterSuccess = {
                navController.navigate(Routes.Home.name) {
                    popUpTo(Routes.Signup.name) { inclusive = true }
                }
            }
        )
    )

    var isRegistered by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()

    val onLogout = {
        isRegistered = false
        scope.launch {
            dataStoreManger.clearDataStore()
        }
    }
    // Determine the start destination based on the user's registration state
    var startDestination by remember { mutableStateOf(Routes.Signup.name) }

    // Check the registration state when the app starts
    LaunchedEffect(Unit) {
        checkRegisterState(preferenceDataStore) { isUserRegistered ->
            isRegistered = isUserRegistered
            startDestination = if (isUserRegistered) Routes.Home.name else Routes.Signup.name
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination, // Dynamically set the start destination
        modifier = Modifier
    ) {

        composable(
            route = Routes.Signup.name
        ) {
            SignUp(
                onClickToOTP = { navController.navigate(Routes.Otp.name) },
                authViewModel = authViewModel,
                onLoginClicked = { navController.navigate(Routes.Login.name){
                    popUpTo(Routes.Signup.name){inclusive = true}
                } },
            )
        }
        composable(
            route = Routes.Login.name
        ){
            Login(
                onLoginClicked = {
                    navController.navigate(Routes.Home.name) {
                        popUpTo(Routes.Login.name) { inclusive = true } // Clear Login from the back stack
                    }
                },
                onRegisterClick = {navController.navigate(Routes.Signup.name)}
            )
        }
        composable(
            route = Routes.Otp.name
        ){
            Otp(
                onVerifyClicked = {
                    navController.navigate(Routes.Login.name)
                },
                authViewModel = authViewModel
            )
        }
        composable(route = Routes.Home.name){
            HomeScreen(
                userDetails = dataStoreManger.getFromDataStore()
            )
        }
        composable(route=Routes.ProfileSetting.name){
            ProfileSettingScreen()
        }

    }
}


suspend fun checkRegisterState(
    preferenceDataStore: DataStore<Preferences>,
    onResult: (Boolean) -> Unit
) {
    val preferences = preferenceDataStore.data.first()
    val email = preferences[EMAIL]
    val isRegistered = email != null
    onResult(isRegistered)
}