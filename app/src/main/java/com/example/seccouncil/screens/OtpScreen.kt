package com.example.seccouncil.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.BottomContent
import com.example.seccouncil.common.BottomText
import com.example.seccouncil.common.Dividerr
import com.example.seccouncil.ui.theme.urbanist


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Otp(
    onVerifyClicked:()->Unit = {}
){
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .navigationBarsPadding()
    ){
        OtpContent(onVerifyClicked = onVerifyClicked)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpContent(
    onVerifyClicked:()->Unit = {}
){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 15.dp)
    ) {
        Text(
            text = "OTP Verification",
            style = TextStyle(
                fontSize = 32.sp,
                fontFamily = urbanist,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .padding(top = 80.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Enter the verification code we just sent on your \nemail address",
            style = TextStyle(
                fontFamily = urbanist,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            ),
            color = colorResource(R.color.forgetPassword)
        )
    }
    Spacer(modifier = Modifier.height(32.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){
            OtpBox(imeAction = ImeAction.Next)
            OtpBox(imeAction = ImeAction.Next)
            OtpBox(imeAction = ImeAction.Next)
            OtpBox(imeAction = ImeAction.Done)
        }
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onVerifyClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
            ,
            colors = ButtonColors(
                containerColor = Color.Black,
                disabledContainerColor = Color.Black,
                contentColor = Color.White,
                disabledContentColor = Color.White
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
    modifier: Modifier = Modifier,
    imeAction: ImeAction,
    otpValue: String = "",
    onOtpValueChange: (String) -> Unit = {"1"}
) {
    var isFocused by remember { mutableStateOf(false) }

    TextField(
        value = otpValue,
        onValueChange = onOtpValueChange,
        modifier = modifier
            .size(width = 70.dp, height = 60.dp)
            .clip(RoundedCornerShape(10.dp))
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .border(
                BorderStroke(
                    width = 2.dp,
                    brush = if (isFocused || otpValue.isNotEmpty()) {
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
            imeAction = imeAction
        )
    )
}
