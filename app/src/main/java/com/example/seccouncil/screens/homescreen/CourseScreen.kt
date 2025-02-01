package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.ButtonComm
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.SearchBar
import com.example.seccouncil.common.TextComm


@Preview(showSystemUi = true)
@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    onEnrollClicked:()->Unit = {},
    onBackClicked:()->Unit = {}
){
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ){
//        Spacer(Modifier.height(16.dp))
        SearchBar()
        Spacer(Modifier.height(32.dp))
        Column(
            modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.Start
        ) {
            CourseContent()
            CourseContent()
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CourseContent(
    img:Int = R.drawable.c0,
    contentDescription:String = "Fundamentals of Cyber Security"
){
Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.Start
) {
    Image(
        painter = painterResource(img),
        contentDescription = contentDescription,
        modifier = Modifier
//            .size(width = 343.dp, height = 175.dp)
            .height(175.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp)),
        contentScale = ContentScale.FillWidth
    )
    Spacer(Modifier.height(16.dp))
    TextComm(
        text = contentDescription,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 20.88.sp
    )
    Spacer(Modifier.height(8.dp))
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
    Spacer(Modifier.height(16.dp))
    ButtonComm(
        text = "Rs 25,000/-"
    )
}
}