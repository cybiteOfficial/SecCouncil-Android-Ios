package com.example.seccouncil.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seccouncil.R
import com.example.seccouncil.common.BottomContent
import com.example.seccouncil.common.BottomText
import com.example.seccouncil.common.CountryPicker
import com.example.seccouncil.common.Dividerr
import com.rejowan.ccpc.Country


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUp(
    onClickToOTP:()->Unit={},
    authViewModel: SignUpViewModel = viewModel(),
    onLoginClicked:()->Unit = {}
) {
    val configuration = LocalConfiguration.current // Get screen size
    val screenHeight = configuration.screenHeightDp.dp

    val email by authViewModel.email
    val password by authViewModel.password
    val confirmPassword by authViewModel.confirmPassword
    val errorMessage by authViewModel.errorMessage
    val navigateToOtpScreen by authViewModel.navigateToOtpScreen
    val firstname by authViewModel.firstname
    val lastname by authViewModel.lastname
    val phoneNumber by authViewModel.phoneNumber
    val countryCode by authViewModel.countryCode

    if (navigateToOtpScreen) {
        onClickToOTP()
        authViewModel.navigateToOtpScreen.value = false // Reset navigation state
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ){
        innerPadding->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(state = rememberScrollState())
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SignUpContent(
                authViewModel = authViewModel,
                email = email,
                password = password,
                firstname = firstname,
                lastname = lastname,
                onFirstnameChange = {authViewModel.onFirstnameChange(it)},
                onLastnameChange = {authViewModel.onLastnameChange(it)},
                confirmPassword = confirmPassword,
                onEmailChange = {authViewModel.onEmailChange(it)},
                onPasswordChange = {authViewModel.onPasswordChange(it)},
                onConfirmPasswordChange = {authViewModel.onConfirmPasswordChange(it)},
                onRegisterClick = {authViewModel.senDOtp()},
                phoneNumber = phoneNumber,
                country = countryCode,
                onPhoneNumberChange = {authViewModel.onPhoneNumberChange(it)},
                onCountryCodeChange = {authViewModel.onCountryCodeChange(it)}

            )

            BottomContent()
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }
            Spacer(Modifier.height(screenHeight * 0.015f))
            BottomText(
                normaltext = "Already have an account?",
                clickabletext = "Login Now",
                onClick = onLoginClicked
            )
            Spacer(Modifier.height(if (screenHeight < 600.dp) 16.dp else 28.dp)) // ✅ Adaptive spacing
        }
        if (authViewModel.isLoading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}

@Composable
fun SignUpContent(
    authViewModel: SignUpViewModel,
    email: String,
    password: String,
    confirmPassword: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onRegisterClick: () -> Unit,
    firstname:String,
    lastname:String,
    onFirstnameChange:(String)->Unit,
    onLastnameChange:(String)->Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    country: Country,
    onCountryCodeChange:(Country)->Unit
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp


        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Hello! Register to get\nstarted",
                style = TextStyle(
                    fontSize = if (screenWidth < 360.dp) 24.sp else 32.sp, // ✅ Adaptive font size,
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(start = 15.dp, top = screenHeight * 0.08f)
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.04f)) // ✅ Dynamic spacing
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 25.dp, end = 15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ){
                InputField(
                    placeHolderText = "First name",
                    modifier = Modifier.weight(1f),
                    imeAction = ImeAction.Next,
                    value = firstname,
                    onValueChange = onFirstnameChange,
                    endPadding = 0.dp,
                    startPadding = 0.dp
                )
                InputField(
                    placeHolderText = "Last name",
                    modifier = Modifier.weight(1f),
                    imeAction = ImeAction.Next,
                    value = lastname,
                    onValueChange = onLastnameChange,
                    endPadding =4.dp,
                    startPadding = 0.dp
                )
            }

            Spacer(modifier = Modifier.height(screenHeight * 0.015f))
            InputField(
                placeHolderText = "Email",
                imeAction = ImeAction.Next,
                value = email,
                onValueChange = onEmailChange
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.015f))
            CountryPicker(
                        phoneNumber = phoneNumber,
                country = country,
                onPhoneNumberChange = onPhoneNumberChange,
                onCountryCodeChange = onCountryCodeChange
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.015f))
            InputField(
                placeHolderText = "Password",
                imeAction = ImeAction.Next,
                value = password,
                onValueChange = onPasswordChange
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.015f))
            InputField(
                placeHolderText = "Confirm password",
                imeAction = ImeAction.Done,
                value = confirmPassword,
                onValueChange = onConfirmPasswordChange
            )
            Spacer(modifier = Modifier.height(screenHeight * 0.05f))
            Button(
                onClick = onRegisterClick,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .align(Alignment.CenterHorizontally)
                    .height(if (screenHeight < 600.dp) 45.dp else 50.dp), // ✅ Adjust height for compact screens,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                ),
                enabled = !authViewModel.isLoading.value
            ) {
                Text(
                    text = "Register",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = if (screenWidth < 360.dp) 14.sp else 16.sp, // ✅ Adaptive button text size
                )
            }
            Spacer(modifier = Modifier.height(screenHeight * 0.03f))
            Dividerr()
            Spacer(Modifier.height(screenHeight * 0.015f))
        }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    placeHolderText:String,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(R.color.edit_box_background),
    value:String = "",
    onValueChange:(String)->Unit = {""},
    startPadding: Dp = 25.dp,
    endPadding: Dp = 15.dp
){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(0.99f)
            .padding(start = startPadding, end = endPadding)
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        placeholder = { Text(
            text = placeHolderText,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.place_holder),
            fontSize = if (screenWidth < 360.dp) 12.sp else 14.sp, // ✅ Adaptive text size
        ) },
        maxLines = 1
    )
}



