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
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.ui.theme.urbanist

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
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // Set the background color of the TopAppBar
                    titleContentColor = Color.Black, // Color for the title
                    actionIconContentColor = Color.Black, // Color for the action icons like bell and profile
                    navigationIconContentColor = Color.Black // Color for the navigation icon, if any
                ),
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
                        .size(120.dp)
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
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "alexj1234@gmail.com",
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    color = Color.Gray

                )
            }

           Button(
               onClick = {},
               modifier = Modifier
                   .fillMaxWidth(),
               colors = ButtonColors(
                   containerColor = Color.Black,
                   disabledContentColor = Color.White,
                   contentColor = Color.White,
                   disabledContainerColor = Color.Black
               ),
               shape = RoundedCornerShape(10.dp)

           ) {
               Text(
                   "Edit Profile",
                   style = TextStyle(
                       fontFamily = urbanist
                   )
               )
           }
            Spacer(Modifier.height(10.dp))
            TextComm(
                text = "Your Learning Process",
                fontWeight = FontWeight.Bold,
                modifier = modifier.align(Alignment.Start)
            )
            ProfileContent()
            ProfileContent(
                title = "Minutes watched",
                number = "1400"
            )
        }
    }

}

@Composable
private fun ProfileContent(
    modifier: Modifier = Modifier,
    title:String = "Course Completed",
    number:String = "2",
){
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(top = 5.dp),
        horizontalAlignment = Alignment.Start
    ){
        TextComm(
            text = title,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(16.dp))
        TextComm(
            text = number,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp
        )
        Spacer(Modifier.height(8.dp))
        BasicText(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Gray)) {
                    append("last 30 days ")
                }
                withStyle(style = SpanStyle(color = colorResource(R.color.increment), fontWeight = FontWeight.Bold)) {
                    append(" +100%")
                }
            },
            style = TextStyle(fontSize = 16.sp, fontFamily = urbanist)
        )
    }

}


