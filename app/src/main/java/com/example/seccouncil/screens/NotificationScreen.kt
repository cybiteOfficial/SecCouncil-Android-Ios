package com.example.seccouncil.screens

import android.icu.text.DateFormat
import androidx.annotation.DrawableRes
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import java.util.Date


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NotificationScreen(
    modifier: Modifier=Modifier,
    onBackClicked:()->Unit = {}
){
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        topBar = {
            TopAppBar(
               title = {
                   Row(
                       modifier = Modifier.fillMaxWidth(),
                       horizontalArrangement = Arrangement.Center,
                       verticalAlignment = Alignment.CenterVertically
                   ) {
                      Text(
                          text = "Notification",
                          textAlign = TextAlign.Center,
                          fontSize = 20.sp,
                          fontFamily = FontFamily.Serif,
                          fontWeight = FontWeight.ExtraBold
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
    ) {innerpadding->
        Column(
            modifier=modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerpadding)
                .padding(horizontal = 15.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(20.dp,Alignment.Top)
        ) {
            Heading(heading = "New")
            NotificationContent()
            NotificationContent(
                title = "Congrats on finishing course!",
                img = R.drawable.congrate
            )
            NotificationContent(
                title = "Limite time offer!",
                img = R.drawable.discount
            )
            Heading(modifier = Modifier.padding(top = 10.dp, bottom = 5.dp),
                heading = "Old")
            NotificationContent(
                title = "Payment successful",
                img = R.drawable.transaction
            )
            NotificationContent(
                title = "Credit card connected!",
                img = R.drawable.credit_card
            )
        }
    }



}

@Composable
fun NotificationContent(
    modifier: Modifier = Modifier,
    @DrawableRes img:Int = R.drawable.brand_new,
    title:String = "New course available!",
    description:String = "Try this now fkg kgowp k kglrk knfn knwnanknvaca\n" +
            "fjajf kn fjc n jakns knknlan nkkanln klnkal\n" +
            "falf kkjabj kbjabjkb cmnklaljnklfn ln nn llalfn.",
    date:DateFormat = DateFormat.getDateInstance(DateFormat.SHORT)
){
    val currentDate = Date()
    Box(
        modifier = Modifier.fillMaxWidth()
            .border(0.5.dp, Color.Gray, RoundedCornerShape(10.dp))
    ){
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(5.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Image(
                painter = painterResource(img),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)

            ) {
                Row(
                    modifier = modifier.fillMaxWidth()
                    ,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = date.format(currentDate),
                        color = Color.Gray,
                        modifier = Modifier,
                        fontSize = 10.sp
                    )
                }
                Spacer(Modifier.height(10.dp))
                Text(
                    text = description,
                    fontFamily = FontFamily.Serif,
                    color = colorResource(R.color.place_holder),
                    fontSize = 12.sp
                )
                Spacer(Modifier.height(3.dp))
            }

        }
    }

}


@Composable
private  fun Heading(
    modifier:Modifier = Modifier,
    heading:String
){
    Row(
        modifier = modifier.fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Text(
            text = heading,
            fontSize = 20.sp,
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Mark all as read",
            color = colorResource(R.color.markallasread),
            modifier = Modifier,
            fontSize = 12.sp
        )
    }
}