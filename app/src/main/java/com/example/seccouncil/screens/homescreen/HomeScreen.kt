package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.ui.theme.urbanist


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
        NavItem("Courses", icon = R.drawable.school),
        NavItem("Downloads", icon = R.drawable.download),
        NavItem("Profile", icon = R.drawable.profile)
    )
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

        Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White, // Set the background color of the TopAppBar
                        titleContentColor = Color.Black, // Color for the title
                        actionIconContentColor = Color.Black, // Color for the action icons like bell and profile
                        navigationIconContentColor = Color.Black // Color for the navigation icon, if any
                    ),
                    title = {
                        TopContent(
                            onBellClicked = onBellClicked,
                            onProfileClicked = onProfileClicked
                        )
                    }
                )
            },
            containerColor = Color.White,
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White
                ) {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = { selectedIndex = index },
                            icon = {
                                Icon(
                                    painter = painterResource(id = navItem.icon),
                                    contentDescription = null,
                                    modifier = Modifier.size( 24.dp)
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
                                        .offset(y =  (-6).dp)
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
            .background(Color.White)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        AdBanner()
        Spacer(Modifier.height(20.dp))
        PopularCourses(
            onCourseClicked = onCourseClicked
        )
        Spacer(Modifier.height(20.dp))
        PopularCourses(title = "Explore Courses")
        Spacer(Modifier.height(15.dp))

    }
}

@Composable
private fun TopContent(
    modifier: Modifier = Modifier,
    onProfileClicked:()->Unit = {},
    onBellClicked:()->Unit = {}
){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(end = 15.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
            Column(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier=Modifier.height(5.dp))
                Text(
                    "Good Morning",
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier=Modifier.height(1.dp))
                Text(
                    "Alex Jackson",
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Icon(
                imageVector = Icons.Outlined.Notifications,
                contentDescription = "Notification",
                modifier = Modifier.size(30.dp)
                    .clickable { onBellClicked() }
            )
        }
    }

@Composable
private fun AdBanner(
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clip(RoundedCornerShape(12.dp)) // Clip the content
            .drawBehind { // Custom stroke effect
                val strokeWidth = 3.dp.toPx() // Stroke width
                val cornerRadius = 12.dp.toPx() // Corner radius
                drawRoundRect(
                    color = Color.Gray, // Stroke color
                    size = size.copy(
                        width = size.width - strokeWidth,
                        height = size.height - strokeWidth
                    ),
                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                    style = Stroke(width = strokeWidth)
                )
            }
            .background(Color.White) // Background color inside stroke
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = { "" },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .clip(RoundedCornerShape(10.dp)), // Inner content clipping
            placeholder = {
                Text(
                    text = "Search courses",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(24.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }

}

@Composable
private fun PopularCourses(
    title:String = "Popular Courses",
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
                text = title,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = urbanist,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "View all",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Course(Modifier.weight(1f),
                courseName = "Cyber Security",
                onCourseClicked = onCourseClicked
            )
            Spacer(Modifier.width(15.dp))
            Course(Modifier.weight(1f),
                courseName = "Computer Networking",
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
            Color.White,
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
                    .size(164.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        onCourseClicked()
                    }

            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "${courseName}",
                style = TextStyle(
                    fontFamily = urbanist,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun CourseContent(
//    modifier: Modifier = Modifier,
//    rating: String = "4.7",
//    no_of_students: String = "1,102",
//    madeBy: String = "Aadil Aariz",
//    subject: String = "Coding"
//) {
//    Column(
//        horizontalAlignment = Alignment.Start,
//        modifier = modifier
//    ) {
//        // First row: Rating | Students
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "Rating $rating/5",
//                color = colorResource(R.color.place_holder),
//                fontFamily = FontFamily.Serif,
//                fontSize = 12.sp
//            )
//            Spacer(Modifier.width(5.dp))
//            VerticalDivider(
//                modifier = Modifier
//                    .height(12.dp),
//                thickness = 1.dp,
//                color = Color.DarkGray
//            )
//            Spacer(Modifier.width(5.dp))
//            Text(
//                text = "$no_of_students students",
//                color = colorResource(R.color.place_holder),
//                fontFamily = FontFamily.Serif,
//                fontSize = 12.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.height(5.dp))
//
//        // Second row: MadeBy | Subject
//        Row(
//            modifier = Modifier.fillMaxWidth(),
//            horizontalArrangement = Arrangement.Start,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(
//                text = "$madeBy",
//                color = colorResource(R.color.place_holder),
//                fontFamily = FontFamily.Serif,
//                fontSize = 12.sp
//            )
//            Spacer(Modifier.width(5.dp))
//            VerticalDivider(
//                modifier = Modifier
//                    .height(12.dp),
//                thickness = 1.dp,
//                color = Color.DarkGray
//            )
//            Spacer(Modifier.width(5.dp))
//            Text(
//                text = "$subject",
//                color = colorResource(R.color.place_holder),
//                fontFamily = FontFamily.Serif,
//                fontSize = 12.sp
//            )
//        }
//    }
//}
