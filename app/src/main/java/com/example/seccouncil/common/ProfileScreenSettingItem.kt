package com.example.seccouncil.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R


@Preview(showBackground = true)
@Composable
fun ProfileScreenSettingItem(
    modifier: Modifier = Modifier,
    @DrawableRes image:Int = R.drawable.profile,
    title:String = "Name",
    description: String = "Alex Jack son"
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(40.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(15.dp))
            TextBind(title = title, description = description)
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
                modifier = Modifier
                    .size(32.dp),
                tint = Color.Gray
            )
        }
        Spacer(Modifier.height(5.dp))
        HorizontalDivider(
            color = Color.LightGray
        )
    }
}

@Composable
fun TextBind(
    title:String = "Name",
    description:String = "Alex Jack Son"
){
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Thin
        )
        Text(
            text = description,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}