package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.paddingFrom
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.seccouncil.R
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.LinearProgressBarWithCircle
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.SearchBar
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.common.ViewAllTitle
import com.example.seccouncil.screens.DownloadScreen
import com.example.seccouncil.screens.profilesetting.ProfileScreen
import com.example.seccouncil.ui.theme.urbanist


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen() {
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
    Scaffold(
            modifier = Modifier
                .fillMaxSize(),
            topBar = {
                if (currentDestination == "home") {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White, // Background color
                            titleContentColor = Color.Black, // Title color
                            actionIconContentColor = Color.Black, // Action icon color
                            navigationIconContentColor = Color.Black // Navigation icon color
                        ),
                        title = {
                            TopContent()
                        }
                    )
                }
            },
            containerColor = Color.White,
            bottomBar = {
                    BottomNavigationBar(navController)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") { ContentScreen(onCourseClicked = { navController.navigate("courses") }) }
                composable("courses") { CourseScreen(onBackClicked = { navController.popBackStack() }) }
                composable("profile") { ProfileScreen() }
                composable("downloads"){ DownloadScreen() }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentScreen(modifier: Modifier = Modifier,
                  onCourseClicked: () -> Unit = {},
                  enrolled:Boolean = true
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
        SearchBar()
        Spacer(Modifier.height(20.dp))
        if(enrolled){
           ViewAllTitle(title = "Ongoing Courses")
            Spacer(Modifier.height(8.dp))
            OnGoingCourses()
            Spacer(Modifier.height(16.dp))
        }
        ViewAllTitle(title = "Popular Courses")
        Spacer(Modifier.height(16.dp))
        PopularCourses(
            onCourseClicked = onCourseClicked
        )
        Spacer(Modifier.height(20.dp))
        ViewAllTitle(title = "Explore Courses")
        Spacer(Modifier.height(16.dp))
        PopularCourses()
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
private fun OnGoingCourses(
    modifier: Modifier = Modifier,
    title: String = "Cyber Security Beginner",
    description:String = "Explore Cybersecurity courses that focus on skills like threat detection, network security, and ...",
    videosLeft:Int = 9
){

    Card(
        modifier = modifier.wrapContentWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4776E6), Color(0xFF20408B)),
                    start = Offset(0f,0f),
                    end = Offset(100f,1100f)
                ),
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent,
            contentColor = Color.White)

    ){
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            TextComm(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(Modifier.height(12.dp))
            TextComm(
                text = description,
                fontSize = 12.sp
            )
            Spacer(Modifier.height(8.dp))
            RatingContent()
            Spacer(Modifier.height(8.dp))
            EnrolledContent()
            Spacer(Modifier.height(8.dp))
            LinearProgressBarWithCircle(finished = 5, total = 14)
            Spacer(Modifier.height(2.dp))
            Row(){
                    Spacer(Modifier.weight(1f))
                Text(
                    text = "$videosLeft videos left",
                    color = Color.LightGray,
                    fontSize = 12.sp,
                    fontFamily = urbanist,
                    modifier = Modifier
                        .padding(end = 20.dp)
                )
            }
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
            modifier = Modifier.fillMaxWidth()
        ) {
            Course(
                courseName = "Cyber Security",
                onCourseClicked = onCourseClicked
            )
            Spacer(Modifier.weight(1f))
            Course(
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
            Spacer(Modifier.weight(1f))
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
        .wrapContentWidth()
         // Add a border with rounded corners
        , // Clip the content to the rounded border
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
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

@Composable
fun BottomNavigationBar(
    navController: NavController = rememberNavController()
) {
    val navItemList = listOf(
        NavItem("Home", icon = R.drawable.home), // Replace with your icons
        NavItem("Courses", icon = R.drawable.school),
        NavItem("Downloads", icon = R.drawable.download),
        NavItem("Profile", icon = R.drawable.profile)
    )
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // NavigationBar is a container for bottom navigation items.
    NavigationBar {
        // Loop through each navigation item (route and icon) from the list.
        navItemList.forEach { (route, icon) ->
            // Add a NavigationBarItem for each item in the list.
            NavigationBarItem(
                // Mark the item as selected if it matches the current route.
                selected = currentRoute == route,
                // Define what happens when the navigation item is clicked.
                onClick = {
                    // Navigate to the corresponding route.
                    navController.navigate(route) {
                        // Clear the back stack up to the start destination to avoid duplicate entries.
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true // Save the state of the previous destination.
                        }
                        launchSingleTop = true // Prevent creating multiple instances of the same destination.
                        restoreState = true // Restore the saved state of the destination when revisiting it.
                    }
                },
                // Define the icon to display for the navigation item.
                icon = {
                    Icon(
                        painter = painterResource(icon), // Load the icon from resources.
                        contentDescription = null, // Provide accessibility (null here since it's decorative).
                        modifier = Modifier.size(24.dp) // Set the size of the icon to 24dp.
                    )
                },
                // Define the label for the navigation item.
                label = {
                    Text(
                        text = route, // Display the route name as the label.
                        modifier = Modifier
                            .offset(y = (-6).dp) // Slightly adjust the label's position vertically.
                    )
                }
            )
        }
    }
}