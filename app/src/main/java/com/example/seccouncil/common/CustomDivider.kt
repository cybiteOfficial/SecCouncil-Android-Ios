package com.example.seccouncil.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.ui.theme.urbanist


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    normaltext:String ="Hello",
    clickabletext:String ="hello",
    onLoginClick:()->Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Column(
        modifier = Modifier.wrapContentHeight()
            .padding(horizontal = 15.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Img_in_Box(
                img = R.drawable.facebook,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(screenWidth*0.015f))
            Img_in_Box(
                img = R.drawable.google,
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(screenWidth*0.015f))
            Img_in_Box(
                img = R.drawable.apple,
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Img_in_Box(
    modifier: Modifier = Modifier,
    @DrawableRes img :Int = R.drawable.google
){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Box(
        modifier = Modifier
            .size(
                height = screenWidth * 0.15f, // Responsive height
                width = screenWidth * 0.3f   // Responsive width
            )
            .border(0.5.dp,Color.Gray, RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))

    ){
        Icon(
            painter = painterResource(img),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center),
            tint = Color.Unspecified
        )
    }
}
@Composable
fun BottomText(
    modifier: Modifier = Modifier,
    normaltext:String = "Hello",
    clickabletext:String = "Hello",
    onClick:()->Unit = {}
){
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val fontSizeSp = with(LocalDensity.current) { (screenWidth * 0.045f).toSp() } // Convert Dp to Sp
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center

    ) {
        Text(
            text = normaltext,
            style = TextStyle(
                fontFamily = urbanist,
                fontWeight = FontWeight.Medium,
                fontSize = fontSizeSp // Responsive text size
            )
        )
        GradientClickableText(clickabletext = clickabletext, onLoginClick = onClick)
    }
}


@Composable
fun GradientClickableText(
    clickabletext: String,
    onLoginClick: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val fontSizeSp = with(LocalDensity.current) { (screenWidth * 0.045f).toSp() } // Convert Dp to Sp

    BasicText(
        text = buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF39B6FF), // Start color
                            Color(0xFF5271FF)  // End color
                        )
                    ),
                    fontSize = fontSizeSp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = urbanist
                )
            ) {
                append(clickabletext)
            }
        },
        modifier = Modifier
            .padding(start = screenWidth * 0.015f)
            .clickable(onClick = onLoginClick)
    )
}
