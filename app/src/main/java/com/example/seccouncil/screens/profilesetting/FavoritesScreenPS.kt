package com.example.seccouncil.screens.profilesetting

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.TopAppBar

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FavoritesScreenPs(
    modifier: Modifier = Modifier,
){
    TopAppBar(
        modifier = Modifier,
        title = "Favorites",
        onClick = {},
        content = { FavoritesScreenPsContent() }
    )
}

@Composable
fun FavoritesScreenPsContent(
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
            .border(0.5.dp, Color.Black, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.c1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(147.dp)
                    .clip(RoundedCornerShape(10.dp))
                ,
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.height(5.dp))
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
                ,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "Cyber Security \nBegineer",
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.star_2),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}