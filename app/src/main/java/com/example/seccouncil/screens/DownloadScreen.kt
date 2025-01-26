package com.example.seccouncil.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.common.TextComm


@Preview(showSystemUi = true)
@Composable
fun DownloadScreen(){
        Column(
            modifier = Modifier
                .safeDrawingPadding()
                .fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            TextComm(
                text = "Downloads",
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 15.dp),
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray
            )
            Spacer(Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topEnd = 20.dp, topStart = 20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ){
                TextComm(
                    text = "Fundamentals of Cyber Security",
                    modifier = Modifier.padding(start = 15.dp, top = 15.dp),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(15.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DownloadContent()
                    DownloadContent()
                    DownloadContent()
                    DownloadContent()
                    DownloadContent()


                }
            }
        }
}



@Preview(showBackground = true)
@Composable
private fun DownloadContent(){
    Column(){
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(width = 0.5.dp,shape = RoundedCornerShape(20.dp), color = Color.LightGray)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray)
            ){
            }
            Spacer(Modifier.width(5.dp))
            Column(
                modifier = Modifier.wrapContentSize(),
                horizontalAlignment = Alignment.Start
            ){
                TextComm(
                    text = "Getting Started",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 14.4.sp
                )
                TextComm(
                    text = "Chapter 1",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 14.4.sp
                )
            }
            Spacer(Modifier.weight(1f))
            TextComm(
                text = "200MB",
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.width(12.dp))
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = ""
            )
        }
        Spacer(Modifier.height(12.dp))
        HorizontalDivider()
    }

}
