package com.example.seccouncil.screens


import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.seccouncil.R
import com.example.seccouncil.common.BottomContent
import com.example.seccouncil.common.BottomText
import com.example.seccouncil.common.Dividerr
import com.example.seccouncil.ui.theme.urbanist
import kotlin.math.sign

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Otp(
    onVerifyClicked: () -> Unit = {},
    authViewModel: SignUpViewModel = viewModel()
) {
    // Observing otpUser from the ViewModel
    val otpUser by authViewModel.otpUser
    var validOtp by authViewModel.validOtp // Assuming LiveData
    val errorMessage by authViewModel.errorMessage
    val signUpMessage by authViewModel.signUpMessage
    val shouldCloseApp by authViewModel.shouldCloseApp
    val context = LocalContext.current

    LaunchedEffect(key1 = validOtp) {
        if (validOtp) {
            onVerifyClicked()
            authViewModel.validOtp.value = false
        }
    }

    BackHandler(enabled = true) {
        authViewModel.setShouldCloseApp(true)
    }
    val activity = (LocalContext.current as? Activity)
    BackHandler(enabled = true) {
        activity?.finish()
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .navigationBarsPadding()
        ) {
            OtpContent(
                otpUser = otpUser,
                viewModel = authViewModel,
                errorMessage = errorMessage,
                signUpMessage = signUpMessage,
                modifier = Modifier.align(Alignment.TopStart)
            )

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpContent(
    viewModel: SignUpViewModel,
    otpUser: String,
    errorMessage: String,
    signUpMessage:String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        val context = LocalContext.current
        Text(
            text = "OTP Verification",
            style = TextStyle(
                fontSize = 32.sp,
                fontFamily = urbanist,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(top = 80.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Enter the verification code we just sent on your \nemail address",
            style = TextStyle(
                fontFamily = urbanist,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp
            ),
            color = colorResource(R.color.forgetPassword)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // OTP input box with focus handling
        OtpBox(
            otpUser = otpUser,
            viewModel = viewModel
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    fontSize = 12.sp,
                    color = Color.Red
                )
            }
            if (signUpMessage.isNotEmpty()) {
                Toast.makeText(context, "Sign-In Successfully", Toast.LENGTH_SHORT).show()
            }
        }
        Spacer(Modifier.height(40.dp))

        // Verify button
        Button(
            onClick = { viewModel.validateOtp()},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Verify",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
        }

        Spacer(Modifier.height(35.dp))
        Dividerr()
        Spacer(Modifier.height(25.dp))
        BottomContent()

        Spacer(Modifier.weight(1f))
        BottomText(
            normaltext = "Didn't receive OTP?",
            clickabletext = "Resend",
            onClick = {}
        )

        Spacer(Modifier.height(28.dp))
    }
}

@Composable
fun OtpBox(
    viewModel: SignUpViewModel,
    modifier: Modifier = Modifier,
    otpUser: String
) {
    var isFocused by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        TextField(
            value = otpUser,
            onValueChange = { viewModel.onOtpUserChange(it) },
            modifier = modifier
                .height(60.dp)
                .clip(RoundedCornerShape(10.dp))
                .onFocusChanged { focusState ->
                    isFocused = focusState.isFocused
                }
                .border(
                    BorderStroke(
                        width = 2.dp,
                        brush = if (isFocused || otpUser.isNotEmpty()) {
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF39B6FF), // Start color
                                    Color(0xFF5271FF)  // End color
                                )
                            )
                        } else {
                            SolidColor(colorResource(R.color.edit_box_background))
                        }
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.edit_box_background),
                unfocusedContainerColor = colorResource(R.color.edit_box_background),
                disabledContainerColor = colorResource(R.color.edit_box_background),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }

}
