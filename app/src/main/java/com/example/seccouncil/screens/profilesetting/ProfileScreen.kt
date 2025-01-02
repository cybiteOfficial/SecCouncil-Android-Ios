package com.example.seccouncil.screens.profilesetting

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.ProfileScreenItem

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onProfileClicked:()->Unit = {},
    onSecurityClicked:()->Unit = {},
    onCertificationsClicked:()->Unit = {},
    onHelpCenterClicked:()->Unit = {},
    onFavouritesClicked:()->Unit = {},
    onBackClicked:()->Unit = {}

){
    val profileOptions = listOf("Profile","Favorites",
        "Security","Certifications",
        "Payment method","Help center",
        "Settings")

    val iconOptions = listOf(R.drawable.profile,R.drawable.heart,
        R.drawable.security_lock,R.drawable.congrate,
        R.drawable.card_payment_2,R.drawable.help,R.drawable.settings)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(end = 15.dp)
                        ,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f)) // Push the icon to the end
                        Icon(
                            imageVector = Icons.Default.Add,
                            modifier = Modifier
                                .size(width = 21.dp, height = 28.dp),
                            contentDescription = "Add More Option"
                        )

                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) {
        innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ){
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .border(0.5.dp, color = Color.White,RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                ){
                    Image(
                        painter = painterResource(R.drawable.__1) ,
                        contentDescription = "profile Picture",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(Modifier.height(5.dp))
                Text(
                    text = "Alex Jackson",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp
                )
                Text(
                    text = "alexj1234@gmail.com",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 12.sp,
                    color = Color.Gray

                )
            }
            Spacer(Modifier.height(5.dp))
            profileOptions.forEachIndexed{
                index,profileoption->
                ProfileScreenItem(
                    image = iconOptions[index],
                    title = profileoption,
                    onClicked = {
                        when(index){
                            0->onProfileClicked()
                            1->onFavouritesClicked()
                            2->onSecurityClicked()
                            3->onCertificationsClicked()
                            5->onHelpCenterClicked()
                        }
                    }
                )
            }
        }
    }

}

