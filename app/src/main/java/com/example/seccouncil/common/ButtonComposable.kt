package com.example.seccouncil.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.ui.theme.urbanist

@Composable
fun ButtonComposable(
    name:String = "Sign Up",
    startPadding: Dp = 15.dp,
    endPadding:Dp = 15.dp,
    containerColor:Color = colorResource(R.color.button_color),
    contentColor:Color = Color.Black,
    fontSize:TextUnit = 20.sp,
    height:Dp = 50.dp,
    fontWeight: FontWeight = FontWeight.Bold
){
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startPadding, end = endPadding)
            .height(height),
        shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
        ,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )

    ) {
        Text(
            text = name,
            fontFamily = urbanist,
            fontWeight = fontWeight,
            fontSize = fontSize
        )
    }
}