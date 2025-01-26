package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.ButtonComposable
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.common.TopAppBar

@Preview(showSystemUi = true)
@Composable
fun CourseDetailScreen(){
    TopAppBar(
        title = "Fundamentals of Cyber Security",
        onClick = {},
        fontSize = 16.sp,
        iconSize = 28.dp,
        showTrailingIcon = false,
        content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.frame_32),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ){
                        EnrolledContent(
                            tint = Color.Black,
                            showEnroll = false
                        )
                        Spacer(Modifier.width(20.dp))
                        RatingContent()
                    }
                    Spacer(Modifier.height(24.dp))
                    ButtonComposable(
                        name = "Buy 25,000 /-",
                        startPadding = 0.dp,
                        endPadding = 0.dp,
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        fontSize = 12.sp,
                        height = 35.dp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.height(35.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CourseDetailContent()
                        CourseDetailContent()
                        CourseDetailContent()
                        CourseDetailContent()
                        CourseDetailContent()
                        CourseDetailContent()
                        CourseDetailContent()
                    }
                }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun CourseDetailContent(
    id:Int = 1,
    title:String = "Getting Started",
    time:Int = 10
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)) // Clip the content
            .drawBehind { // Custom stroke effect
                val strokeWidthPx = 3.dp.toPx()
                val cornerRadiusPx = 10.dp.toPx()
                drawRoundRect(
                    color = Color.Gray, // Stroke color
                    size = size.copy(
                        width = size.width - strokeWidthPx,
                        height = size.height - strokeWidthPx
                    ),
                    topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2),
                    cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                    style = Stroke(width = strokeWidthPx)
                )
            }
            .background(Color.White), // Background color inside stroke
        contentAlignment = Alignment.Center
    ) {
        Row(
           modifier =  Modifier
               .fillMaxWidth()
               .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextComm(
                text = "$id.  $title",
                fontSize = 12.sp,
                lineHeight = 20.sp
            )
            Spacer(Modifier.weight(1f))
            TextComm(
                text = "$time Mins",
                fontSize = 12.sp,
                lineHeight = 20.sp,
                color = Color.Gray
            )
        }
    }
}