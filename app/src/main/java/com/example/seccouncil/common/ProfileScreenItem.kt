package com.example.seccouncil.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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


@Preview(showSystemUi = true)
@Composable
fun ProfileScreenItem(
    modifier: Modifier = Modifier,
    @DrawableRes image:Int = R.drawable.profile,
    title:String = "Profile",
    fontWeight: FontWeight = FontWeight.Black,
    color: Color = Color.Unspecified,
    onClicked:()->Unit = {}
){
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClicked() }
                .height(35.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = title,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(15.dp))
            Text(
                text = title,
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = fontWeight,
                color = color
            )
            Spacer(Modifier.weight(1f))
            Icon(
                imageVector = Icons.Filled.KeyboardArrowRight,
                contentDescription = "KeyboardArrowRight",
                modifier = Modifier
                    .size(32.dp),
                tint = Color.Gray
            )
        }
        Spacer(Modifier.height(15.dp))
        HorizontalDivider(
            color = Color.LightGray
        )
    }
}