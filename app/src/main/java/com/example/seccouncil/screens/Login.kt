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
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.BottomContent
import com.example.seccouncil.common.BottomText
import com.example.seccouncil.common.Dividerr
import com.example.seccouncil.ui.theme.UrbanistTitleStyle
import com.example.seccouncil.ui.theme.urbanist


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Login(
    onLoginClicked:()->Unit ={}
){
    Column(
    modifier = Modifier
        .safeDrawingPadding()
        .navigationBarsPadding()
    ){

        Column(
            modifier = Modifier
                .fillMaxSize(),
        ){
            LoginContent(
                onLoginClicked = onLoginClicked
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun LoginContent(
    onLoginClicked: () -> Unit={}
){
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
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        InputField(placeHolderText = "Enter your email", imeAction = ImeAction.Done)
        Spacer(Modifier.height(16.dp))
        InputField(placeHolderText = "Enter your password", imeAction = ImeAction.Done)
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
            )

        ) {
            Text(
                text = "Login",
                style = UrbanistTitleStyle.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = Color.White
                ,
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
                color = colorResource(R.color.forgetPassword)
            )
        }

        Spacer(Modifier.height(36.dp))
        Dividerr(text = "Or Login with")
        Spacer(Modifier.height(24.dp))
        BottomContent(
            normaltext = "Don't have an account?",
            clickabletext = "Register Now"
        )
        Spacer(Modifier.weight(1f))
        BottomText(
            normaltext = "Don't have an account?",
            clickabletext = "Register Now"
        )
        Spacer(Modifier.height(28.dp))
    }
}
