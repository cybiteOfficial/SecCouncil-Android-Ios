package com.example.seccouncil.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seccouncil.screens.Login
import com.example.seccouncil.screens.NotificationScreen
import com.example.seccouncil.screens.Otp
import com.example.seccouncil.screens.PasswordScreen
import com.example.seccouncil.screens.SearchScreen
import com.example.seccouncil.screens.SignUp
import com.example.seccouncil.screens.SignUpViewModel
import com.example.seccouncil.screens.homescreen.CourseScreen
import com.example.seccouncil.screens.homescreen.HomeScreen
import com.example.seccouncil.screens.payment.PaymentScreen
import com.example.seccouncil.screens.profilesetting.Certification
import com.example.seccouncil.screens.profilesetting.FavoritesScreenPs
import com.example.seccouncil.screens.profilesetting.HelpCenter
import com.example.seccouncil.screens.profilesetting.ProfileScreen
import com.example.seccouncil.screens.profilesetting.ProfileSettingScreen
import com.example.seccouncil.screens.profilesetting.Security

@Composable
fun Navigation(
    navController:NavHostController = rememberNavController()
){

    val authViewModel:SignUpViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Routes.Signup.name,
        modifier = Modifier
    ){
        composable(
            route = Routes.Signup.name
        ){
            SignUp(
                onClickToOTP =
                {navController.navigate(Routes.Otp.name)},
                authViewModel = authViewModel
            )

        }
        composable(
            route = Routes.Login.name
        ){
            Login(
                onLoginClicked = {
                    navController.navigate(Routes.Home.name)
                }
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
        composable(
            route = Routes.Password.name
        ){
            PasswordScreen(
                onLoginClicked = {navController.navigate(Routes.Home.name)}
            )
        }
        composable(route = Routes.Home.name){
            HomeScreen(
                onCertificationClicked = {navController.navigate(Routes.Certification.name)},
                onCourseClicked = {navController.navigate(Routes.Courses.name)},
                onProfileClicked = {navController.navigate(Routes.Profile.name)},
                onSearchClicked = {navController.navigate(Routes.Search.name)},
                onBellClicked = {navController.navigate(Routes.Notification.name)},
                onHomeClicked = {navController.navigate(Routes.Home.name)},
                onProfileSettingClicked = {navController.navigate(Routes.ProfileSetting.name)}
            )
        }
        composable(route = Routes.Courses.name){
            CourseScreen(
                onEnrollClicked = {
                    navController.navigate(Routes.Payment.name)
                },
                onBackClicked = {navController.navigateUp()}
            )
        }
        composable(route = Routes.Payment.name){
            PaymentScreen(
                onBackClicked = {navController.navigateUp()}
            )
        }
        composable(route = Routes.Profile.name){
            ProfileScreen(
                onBackClicked = {navController.navigateUp()},
                onProfileClicked = {navController.navigate(Routes.ProfileSetting.name)},
                onFavouritesClicked = {navController.navigate(Routes.Favourites.name)},
                onSecurityClicked = {navController.navigate(Routes.Security.name)},
                onHelpCenterClicked = {navController.navigate(Routes.HelpCenter.name)},
                onCertificationsClicked = {navController.navigate(Routes.Certification.name)}
            )
        }
        composable(route = Routes.ProfileSetting.name){
            ProfileSettingScreen(
                onBackClicked = {navController.navigateUp()}
            )
        }
        composable(route = Routes.Notification.name){
            NotificationScreen(
                onBackClicked = {navController.navigateUp()}
            )
        }
        composable(route = Routes.Search.name){
            SearchScreen(
                onBackClicked = {navController.navigateUp()}
            )
        }
        composable(route = Routes.Favourites.name){
            FavoritesScreenPs(
                onBackClicked = {navController.navigateUp()}
            )
        }
        composable(route = Routes.Security.name){
           Security(
               onBackClicked = {navController.navigateUp()}
           )
        }

        composable(route = Routes.HelpCenter.name){
          HelpCenter(
              onBackClicked = {navController.navigateUp()}
          )
        }
        composable(route = Routes.Certification.name){
            Certification(
                onBackClicked = {navController.navigateUp()}
            )
        }

    }
}