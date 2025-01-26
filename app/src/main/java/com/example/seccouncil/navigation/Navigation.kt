package com.example.seccouncil.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seccouncil.screens.Login
import com.example.seccouncil.screens.Otp
import com.example.seccouncil.screens.SignUp
import com.example.seccouncil.screens.SignUpViewModel
import com.example.seccouncil.screens.homescreen.HomeScreen

@Composable
fun Navigation(
    navController:NavHostController = rememberNavController()
){

    val authViewModel:SignUpViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Signup.name, // Start with Splash Screen
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
                } }
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
            HomeScreen()
        }
    }
}
