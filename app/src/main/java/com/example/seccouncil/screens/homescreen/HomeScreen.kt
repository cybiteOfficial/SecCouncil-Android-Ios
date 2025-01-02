package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen(
    onHomeClicked:()->Unit = {},
    onCourseClicked:()->Unit = {},
    onCertificationClicked:()->Unit = {},
    onProfileClicked:()->Unit = {},
    onProfileSettingClicked: () -> Unit = {},
    onBellClicked: () -> Unit = {},
    onSearchClicked: () -> Unit={}
) {
    val navItemList = listOf(
        NavItem("Home", icon = R.drawable.home), // Replace with your icons
        NavItem("Courses", icon = R.drawable.courses),
        NavItem("Certification", icon = R.drawable.certification),
        NavItem("Profile", icon = R.drawable.profile)
    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    TopContent(
                        onBellClicked = onBellClicked,
                        onProfileClicked = onProfileClicked,
                        onSearchClicked = onSearchClicked
                    )
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navItemList.forEachIndexed { index, navItem ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = navItem.icon),
                                contentDescription = null,
                                modifier = Modifier.size(if (index != 0) 40.dp else 30.dp)
                                    .clickable {
                                        when(index){
                                            0->onHomeClicked()
                                            1->onCourseClicked()
                                            2->onCertificationClicked()
                                            3->onProfileSettingClicked()
                                        }
                                    }
                            )
                        },
                        label = {
                            Text(
                                text = navItem.label,
                                modifier = Modifier
                                    .offset(y = if (index!=0) (-6).dp else 0.dp)
                            )
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        ContentScreen(
            modifier = Modifier.padding(innerPadding),
            onCourseClicked = onCourseClicked
        )
    }
}

@Composable
fun ContentScreen(modifier: Modifier = Modifier,
                  onCourseClicked: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 15.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(25.dp))
        AdBanner()
        Spacer(Modifier.height(20.dp))
        HorizontalDivider(
            color = Color.Black
        )
        Spacer(Modifier.height(20.dp))
        PopularCourses(
            onCourseClicked = onCourseClicked
        )
        Spacer(Modifier.height(15.dp))
        TopRatedCourses(
            onCourseClicked = onCourseClicked
        )
        Spacer(Modifier.height(15.dp))

    }
}

@Composable
private fun TopContent(
    modifier: Modifier = Modifier,
    onProfileClicked:()->Unit = {},
    onSearchClicked:()->Unit = {},
    onBellClicked:()->Unit = {}
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween

    ){
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(R.drawable.profilepic),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(40.dp)
                    .clickable {
                        onProfileClicked()  // invoking req in lambda funciton not passing
                    }

            )
            Spacer(modifier= Modifier.width(5.dp))
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier=Modifier.height(5.dp))
                Text(
                    "Good Morning",
                    color = colorResource(R.color.place_holder),
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier=Modifier.height(1.dp))
                Text(
                    "Alex Jackson",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }

        }
        Row(
        ){
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                modifier = Modifier
                    .padding(end = 35.dp)
                    .size(30.dp)
                    .clickable { onSearchClicked() }


            )
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notification",
                modifier = Modifier.size(30.dp)
                    .clickable { onBellClicked() }

            )
        }

    }
}

@Composable
private fun AdBanner(
    modifier: Modifier = Modifier
){
    Card(
            modifier = modifier
                .fillMaxWidth(),
        colors = CardColors(
            containerColor = colorResource(R.color.button_color),
            contentColor = colorResource(R.color.black),
            disabledContentColor = colorResource(R.color.black),
            disabledContainerColor = colorResource(R.color.button_color)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp) // Apply padding here
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Todays Special",
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 20.sp
                )
                Text(
                    text = "75% off",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    fontSize = 25.sp
                )
            }
            Spacer(Modifier.height(5.dp))
            Text(
                text = "Hurry! Today's your last chance for a discount.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Light,
                fontFamily = FontFamily.Serif
            )
        }
    }
}

@Composable
private fun PopularCourses(
    modifier: Modifier = Modifier,
    onCourseClicked: () -> Unit = {}
){
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()

            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Popular Courses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "View all",
                color = Color.Blue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Course(Modifier.weight(1f),
                courseName = "Cyber Security \nBegineer",
                onCourseClicked = onCourseClicked
            )
            Spacer(Modifier.width(15.dp))
            Course(Modifier.weight(1f),
                courseName = "Computer \nNetworking",
                onCourseClicked = onCourseClicked
            )
        }
    }

}


@Composable
private fun TopRatedCourses(
    modifier: Modifier = Modifier,
    onCourseClicked: () -> Unit = {}
){
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
            Text(
                text = "Top Rated Courses",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Course(
                Modifier.weight(1f),
                courseName = "Cyber Security \nBegineer",
                onCourseClicked = onCourseClicked
            )
            Spacer(Modifier.width(15.dp))
            Course(Modifier.weight(1f),
                courseName = "Cyber Security \n" +
                    "Begineer",
                onCourseClicked = onCourseClicked)
        }
    }

}

@Composable
private fun Course(
    modifier: Modifier = Modifier,
    courseName:String,
    onCourseClicked:()->Unit = {}
){
    Box(
    modifier = modifier
        .border(
            0.5.dp,
            colorResource(R.color.place_holder),
            RoundedCornerShape(8.dp)
        ) // Add a border with rounded corners
        .clip(RoundedCornerShape(16.dp)), // Clip the content to the rounded border
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(5.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.c0),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(width = 175.dp, height = 118.dp)
                    .clickable {
                        onCourseClicked()
                    }
            )
            Spacer(Modifier.height(5.dp))
            Text(
                text = "${courseName}",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(15.dp))
            CourseContent()
            Spacer(Modifier.height(25.dp))

        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CourseContent(
    modifier: Modifier = Modifier,
    rating: String = "4.7",
    no_of_students: String = "1,102",
    madeBy: String = "Aadil Aariz",
    subject: String = "Coding"
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        // First row: Rating | Students
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rating $rating/5",
                color = colorResource(R.color.place_holder),
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp
            )
            Spacer(Modifier.width(5.dp))
            VerticalDivider(
                modifier = Modifier
                    .height(12.dp),
                thickness = 1.dp,
                color = Color.DarkGray
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "$no_of_students students",
                color = colorResource(R.color.place_holder),
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Second row: MadeBy | Subject
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$madeBy",
                color = colorResource(R.color.place_holder),
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp
            )
            Spacer(Modifier.width(5.dp))
            VerticalDivider(
                modifier = Modifier
                    .height(12.dp),
                thickness = 1.dp,
                color = Color.DarkGray
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "$subject",
                color = colorResource(R.color.place_holder),
                fontFamily = FontFamily.Serif,
                fontSize = 12.sp
            )
        }
    }
}
