package com.example.seccouncil.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R

@Composable
fun ButtonComposable(
    name:String = "Sign Up",

){
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
            text = name,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}