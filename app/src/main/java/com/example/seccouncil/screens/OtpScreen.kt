package com.example.seccouncil.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Otp(){
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .navigationBarsPadding()
    ){
        Card(
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.seec_1),
                contentDescription = null,
                modifier = Modifier.background(color = colorResource(R.color.image_background))
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
        }
        // placing bottom card into top of top card
        Card(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-20).dp) // Adjust the vertical position to overlap the first card
                .clip(RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(R.color.white),
                contentColor = colorResource(R.color.black),
                disabledContentColor = colorResource(R.color.black),
                disabledContainerColor = colorResource(R.color.white)
            )
        ){
            OtpContent()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpContent(){
    Text(
        text = "OTP",
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.Serif,
        fontSize = 25.sp,
        modifier = Modifier.padding(start = 15.dp, top = 30.dp)
    )
    Spacer(modifier = Modifier.height(20.dp))
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Enter OTP",
            fontFamily = FontFamily.Monospace,
            color = colorResource(R.color.place_holder)
        )
        Spacer(Modifier.height(10.dp))
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
        Spacer(Modifier.height(45.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
            ,
            colors = ButtonColors(
                containerColor = colorResource(R.color.button_color),
                disabledContainerColor = colorResource(R.color.button_color),
                contentColor = Color.Black,
                disabledContentColor = Color.Black
            )

        ) {
            Text(
                text = "Log in",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(Modifier.height(35.dp))
        Dividerr()
        Spacer(Modifier.height(25.dp))
        BottomContent(
            normaltext = "Didn't receive OTP?",
            clickabletext = "Resend"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpBox(
    modifier: Modifier = Modifier,
    imeAction: ImeAction
){
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier
            .size(50.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = colorResource(R.color.edit_box_background),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        )
    )
}