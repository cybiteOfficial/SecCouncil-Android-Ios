package com.example.seccouncil.screens.profilesetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.seccouncil.common.TopAppBar

@Preview(showSystemUi= true)
@Composable
fun Certification(){
    TopAppBar(
        title = "Certification",
        modifier = Modifier,
        onClick = {}
        ,
        content = {
               Column(
                   modifier = Modifier.fillMaxWidth()
                       .padding(top = 20.dp),
                   verticalArrangement = Arrangement.spacedBy(20.dp),
                   horizontalAlignment = Alignment.CenterHorizontally
               ) {
                   CertificationItem()
                   CertificationItem()
               }
        }
    )
}
@Composable
private fun CertificationItem(
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.LightGray)
    ){
        Text(
            "View Certification",
            fontFamily = FontFamily.Monospace,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.align(Alignment.Center)
        )
        Icon(
            painter = painterResource(R.drawable.download),
            contentDescription = "Download Certificate",
            modifier = Modifier.size(32.dp)
                .align(Alignment.BottomEnd)
        )
    }
}