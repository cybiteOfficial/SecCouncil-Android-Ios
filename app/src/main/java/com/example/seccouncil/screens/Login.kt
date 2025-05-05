package com.example.seccouncil.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seccouncil.R
import com.example.seccouncil.common.BottomContent
import com.example.seccouncil.common.BottomText
import com.example.seccouncil.common.Dividerr
import com.example.seccouncil.ui.theme.UrbanistTitleStyle
import com.example.seccouncil.ui.theme.urbanist
import com.example.seccouncil.viewmodel.LoginViewModel
import com.example.seccouncil.viewmodel.LoginViewModelFactory


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Login(
    onLoginClicked:()->Unit ={},
    onRegisterClick: () -> Unit = {},
    onforgetPasswordClicked:()-> Unit = {}
){
    val context = LocalContext.current
    val loginViewModel: LoginViewModel = viewModel(factory = LoginViewModelFactory(context))
    val loginEmail by loginViewModel.loginEmail
    val loginPassword by loginViewModel.loginPassword
    val errorMessage by loginViewModel.errorMessage
    val isLoginSuccessful by loginViewModel.isLoginSuccessful
    val navigateToHomeScreen by loginViewModel.navigateToHomeScreen
    val loginMessage by loginViewModel.loginMessage
    val isLoading by loginViewModel.isLoading

    LaunchedEffect(key1 = isLoginSuccessful) {
        if (isLoginSuccessful) {
            onLoginClicked()
            loginViewModel.isLoginSuccessful.value = false
        }
    }
    LaunchedEffect(errorMessage) {
        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            loginViewModel.errorMessage.value = "" // Clear it after showing once
        }
    }

    LaunchedEffect(key1 = navigateToHomeScreen) {
        if (navigateToHomeScreen) {
            Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()
            loginViewModel.navigateToHomeScreen.value = false
        }
    }
    val activity = (LocalActivity.current as? Activity)
    // val activity = (LocalContext.current as? Activity)
    BackHandler(enabled = true) {
        activity?.finish()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
    ) {
        LoginContent(
            onLoginClicked = { loginViewModel.loginUser() },
            loginEmail = loginEmail,
            loginPassword = loginPassword,
            onEmailChange = { loginViewModel.onLoginEmailChange(it) },
            onPasswordChange = { loginViewModel.onLoginPasswordChange(it) },
            errorMessage = errorMessage,
            loginMessage = loginMessage,
            isLoading = isLoading,
            modifier = Modifier.align(Alignment.TopStart),
            onRegisterClick = onRegisterClick,
            onfogetPasswordClicked = onforgetPasswordClicked
        )

        if (isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun LoginContent(
    onLoginClicked: () -> Unit = {},
    loginEmail: String,
    loginPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    errorMessage: String,
    loginMessage: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    onRegisterClick:()->Unit = {},
    onfogetPasswordClicked:()-> Unit = {}
){
    val context = LocalContext.current
    // State to control whether the Terms dialog is visible
    var showTermsDialog by remember { mutableStateOf(false) }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Welcome back! Glad \nto see you, Again!",
            style = TextStyle(
                fontSize = 32.sp,
                fontFamily = urbanist,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(start = 15.dp, top = 80.dp)
        )
        Spacer(modifier = Modifier.height(28.dp))
        InputField(placeHolderText = "Enter your email",
            imeAction = ImeAction.Next,
            value = loginEmail,
            onValueChange = onEmailChange
            )
        Spacer(Modifier.height(16.dp))
        InputField(
            value = loginPassword,
            onValueChange = onPasswordChange,
            placeHolderText = "Enter your password",
            imeAction = ImeAction.Done,
            isPasswordField = true
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onLoginClicked ,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
            ,
            colors = ButtonColors(
                containerColor = Color.Black,
                disabledContainerColor = Color.Black,
                contentColor = Color.Black,
                disabledContentColor = Color.Black
            ),
            enabled = !isLoading

        ) {
            Text(
                text = "Login",
                style = UrbanistTitleStyle.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White,
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp), // Makes the Row take full width
            horizontalArrangement = Arrangement.End // Aligns the content to the end (right)
        ) {
            Text(
                text = "Forget Password?",
                style = TextStyle(
                    fontFamily = urbanist,
                    fontWeight = FontWeight.SemiBold
                ),
                color = colorResource(R.color.forgetPassword),
                modifier = Modifier
                    .clickable(
                        onClick = onfogetPasswordClicked
                    )
            )
        }

        Spacer(Modifier.height(36.dp))
        Dividerr(text = "Or Login with")
        Spacer(Modifier.height(24.dp))
        BottomContent(
            normaltext = "Don't have an account?",
            clickabletext = "Register Now"
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (errorMessage.isNotEmpty()) {
//                Text(
//                    text = errorMessage,
//                    fontSize = 12.sp,
//                    color = Color.Red
//                )
                Toast.makeText(context,errorMessage,Toast.LENGTH_SHORT)
            }
            if (loginMessage.isNotEmpty()) {
                Toast.makeText(context, loginMessage,Toast.LENGTH_SHORT)
            }else{
                Toast.makeText(context,"Something Went Wrong", Toast.LENGTH_SHORT)
            }
        }

        Spacer(Modifier.weight(1f))
        BottomText(
            normaltext = "Don't have an account?",
            clickabletext = "Register Now",
            onClick = onRegisterClick
        )
        // -------------------------------------------------------------------
        // 1. Terms & Conditions Clickable Text
        // -------------------------------------------------------------------
        // We add a centered text that the user can tap to read T&C.
        Text(
            text = "Read Terms and Conditions",
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF39B6FF), // Start color
                        Color(0xFF5271FF)  // End color
                    )
                )
            ),
            textAlign = TextAlign.Center,              // Center the text horizontally
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // When tapped, show the dialog
                    showTermsDialog = true
                }
                .padding(vertical = 12.dp)               // Add some spacing
        )

        // -------------------------------------------------------------------
        // 2. Terms & Conditions Dialog
        // -------------------------------------------------------------------
        // Show an AlertDialog when showTermsDialog is true
        if (showTermsDialog) {
            AlertDialog(
                onDismissRequest = {
                    // Dismiss when the user touches outside or presses back
                    showTermsDialog = false
                },
                title = {
                    Text(
                        text = "Terms and Conditions",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                },
                text = {
                    // Here you can put your full T&C text, or scrollable content.
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(text = "1. You must be at least 18 years old to use this app.", fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "2. Do not share your password with anyone.", fontSize = 14.sp)
                        Spacer(Modifier.height(8.dp))
                        Text(text = "3. We collect anonymous usage data to improve the experience.", fontSize = 14.sp)
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showTermsDialog = false }) {
                        Text(text = "OK")
                    }
                }
            )
        }
        Spacer(Modifier.height(28.dp))
    }
}
