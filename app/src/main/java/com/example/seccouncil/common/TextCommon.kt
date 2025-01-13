package com.example.seccouncil.common

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.seccouncil.ui.theme.urbanist


@Composable
fun TextComm(
    text:String = " ",
    fontSize: TextUnit = 16.sp,
    fontWeight:FontWeight = FontWeight.Normal,
    modifier:Modifier = Modifier
){
    Text(
        text = text,
        style = TextStyle(
            fontFamily = urbanist,
            fontSize = fontSize,
            fontWeight = fontWeight
        ),
        modifier = modifier
    )
}