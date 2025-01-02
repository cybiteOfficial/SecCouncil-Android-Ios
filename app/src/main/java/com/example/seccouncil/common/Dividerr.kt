package com.example.seccouncil.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Dividerr(
    text:String = "or"){
    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 15.dp), // Add vertical padding for spacing
        verticalAlignment = Alignment.CenterVertically // Vertically center content
    ) {
        HorizontalDivider(modifier = Modifier
            .weight(1f)
            .padding(start = 11.dp)
            .fillMaxWidth()
            , color = Color.LightGray
        ) // Line on the left
        Text(
            text=text,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .align(Alignment.CenterVertically), // Text in the middle
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            color = Color.DarkGray,
            fontFamily = FontFamily.Serif
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.LightGray) // Line on the right
    }
}