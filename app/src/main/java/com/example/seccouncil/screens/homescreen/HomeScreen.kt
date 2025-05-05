package com.example.seccouncil.screens.homescreen

import android.app.Activity
import android.content.Context
import android.icu.util.Calendar
import android.icu.util.Currency
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.seccouncil.R
import com.example.seccouncil.common.ButtonComm
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.LinearProgressBarWithCircle
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.ResponsiveSearchBar
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.model.UserDetails
import com.example.seccouncil.navigation.Routes
import com.example.seccouncil.network.getAllCourseDetailsModel.GetAllCourse
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.network.getEnrolledCourse.EnrolledCourse
import com.example.seccouncil.payment_gateway.PaymentViewModel
import com.example.seccouncil.screens.profilesetting.ProfileScreen
import com.example.seccouncil.screens.profilesetting.ProfileSettingScreen
import com.example.seccouncil.ui.theme.urbanist
import com.example.seccouncil.videoplayer.ResponsiveCourseDetailScreenOnPurchase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow


@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreen(
    userDetails: Flow<UserDetails>,
    profileViewmodel: HomescreenViewmodel,
    scope: CoroutineScope,
    paymentViewModel: PaymentViewModel,
    navController2: NavHostController,
    onLogoutClicked: () -> Job
) {
    val navController1 = rememberNavController()
    val currentDestination = navController1.currentBackStackEntryAsState().value?.destination?.route
    val userDetail = userDetails.collectAsState(initial = null)
    val getAllCourseResult = profileViewmodel.getAllCourseResult.collectAsState()
    val courses = profileViewmodel.enrolledCourses.collectAsState()

    // State to track fullscreen mode
    var isFullScreen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentDestination == "home" && !isFullScreen) { // Hide top bar in fullscreen mode
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = Color.Black,
                        actionIconContentColor = Color.Black,
                        navigationIconContentColor = Color.Black
                    ),
                    title = {
                        TopContent(
                            name = userDetail.value?.name ?: ""
                        )
                    }
                )
            }
        },
        containerColor = Color.White,
        bottomBar = {
            if (!isFullScreen) { // Hide bottom bar in fullscreen mode
                BottomNavigationBar(navController = navController1)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController1,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") {
                ContentScreen(
                    onCourseClicked = { navController1.navigate("courseDetail") },
                    getAllCourseResult = getAllCourseResult,
                    profileViewmodel = profileViewmodel,
                    navController = navController1,
                    getEnrolledCourseResult = courses
                )
            }
            composable("courses") {
                CourseScreen(
                    onBackClicked = { navController1.popBackStack() },
                    viewModel = profileViewmodel,
                    navController = navController1
                )
            }
            composable("profile") {
                ProfileScreen(
                    onProfileClicked = { navController1.navigate("profileSetting") },
                    onBackClicked = {navController1.popBackStack()},
                    name = userDetail.value?.name ?: "",
                    email = userDetail.value?.emailAddress ?: "",
                    viewModel = profileViewmodel,
                    onLogoutClicked = {
                        onLogoutClicked()
                        navController2.navigate(Routes.Signup.name) {
                            popUpTo(0) {  // or popUpTo(NavController.graph.startDestinationId)
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    },
                    dob = userDetail.value?.dob?:"",
                    gender = userDetail.value?.gender?:"",
                    phoneNumber = userDetail.value?.mobileNumber?:"",

                  //  scope = scope,
                    about = userDetail.value?.about?:""
                )
            }
            composable("Assignment") {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Assignment")
                }
            }
            composable("profileSetting") {
                ProfileSettingScreen(
                    viewModel = profileViewmodel,
                    //scope = scope,
                    onBackClicked = {navController1.popBackStack()}
                )
            }
            composable("courseDetail/{courseId}") { backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
                ResponsiveCourseDetailScreen(
                    navController = navController1,
                    courseId = courseId,
                    onClick = { navController1.popBackStack() },
                    profileViewmodel = profileViewmodel,
                    email = userDetail.value?.emailAddress ?: "",
                    phone = userDetail.value?.mobileNumber ?: "",
                    paymentViewModel = paymentViewModel
                )
            }
            composable("onPurchasedScreen/{courseId}") {
                    backStackEntry ->
                val courseId = backStackEntry.arguments?.getString("courseId") ?: return@composable
                ResponsiveCourseDetailScreenOnPurchase(
                    videoUri = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
                    isFullScreen = isFullScreen,
                    onFullScreenToggle = { newValue -> isFullScreen = newValue },
                    homescreenViewmodel = profileViewmodel,
                    courseId = courseId
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val navItemList = listOf(
        NavItem("Home", icon = R.drawable.outline_home_24), // Replace with your icons
        NavItem("Courses", icon = R.drawable.school),
        NavItem("Assignment", icon = R.drawable.download),
        NavItem("Profile", icon = R.drawable.outline_person_24)
    )
    val currentRoute = navController.currentBackStackEntry?.destination?.route

    // NavigationBar is a container for bottom navigation items.
    NavigationBar() {
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

fun finishActivity(context: Context){
    val activity = context as? Activity
    activity?.finish()
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    onCourseClicked: () -> Unit = {},
    enrolled: Boolean = true,
    getAllCourseResult: State<NetworkResponse<GetAllCourse>?>,
    getEnrolledCourseResult: State<NetworkResponse<EnrolledCourse>?>,
    profileViewmodel: HomescreenViewmodel,
    navController: NavController
) {
    var searchText by remember { mutableStateOf("") }

    var refreshing by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        profileViewmodel.getAllCourse()
        profileViewmodel.getEnrolledCourse()
    }
    PullToRefreshBox(
        isRefreshing = refreshing,
        onRefresh = {
            refreshing = true
            profileViewmodel.getAllCourse()
        }
    ) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
            .background(Color.White)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))
        ResponsiveSearchBar(
            value = searchText,
            onValueChange = {searchText = it}
        )
        Spacer(Modifier.height(20.dp))
        when (val allCoursesResult = getAllCourseResult.value) {

            is NetworkResponse.Success -> {
                Log.e("ContentScreen", "getAllCourseResult: Success")
                when (val enrolledResult = getEnrolledCourseResult.value) {
                    is NetworkResponse.Success -> {
                        val enrolledCourseIds = enrolledResult.data.data
                        val enrolled: MutableList<String> = mutableListOf()
                        for (i in enrolledCourseIds)
                        {
                            enrolled += (i._id)
                        }
                        Log.e("Hellooo","$enrolled")
                        var allCourse = allCoursesResult.data.data
                        Log.e("Hellooo","$allCourse")

                        val notEnrolledCourses = allCourse.filter { course ->
                            course._id !in enrolled
                        }
                        Log.e("Hellooo","$notEnrolledCourses")

                        if (notEnrolledCourses.isEmpty()) {
                            OnError("No courses available for enrollment.")
                        } else {
                            LazyColumn(
                                modifier = Modifier,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(notEnrolledCourses) { course ->
                                    CourseContent2(
                                        img2 = course.thumbnail,
                                        courseName = course.courseName,
                                        price = course.price.toString(),
                                        noOfStudnets = course.studentsEnrolled.size,
                                        onClick = {
                                            navController.navigate("courseDetail/${course._id}")
                                        },
                                        subjectAuthor = course.instructor.firstName + course.instructor.lastName,
                                        courseId = course._id

                                    )
                                }
                            }
                        }
                    }

                    is NetworkResponse.Error -> {
                        OnError(enrolledResult.message, onClick = { profileViewmodel.getEnrolledCourse() })
                    }

                    else -> OnLoading()
                }
            }

            is NetworkResponse.Error -> {
                Log.e("ContentScreen", "getAllCourseResult: Error: ${allCoursesResult}")
                OnError(allCoursesResult.message, onClick = { profileViewmodel.getAllCourse() })
            }

            is NetworkResponse.Loading -> {
                OnLoading()
                Log.e("ContentScreen", "getAllCourseResult: Loading")
            }
            else -> {
                Log.e("ContentScreen", "getAllCourseResult: Other")
            }
        }
    }}
}

@Composable
private fun TopContent(
    modifier: Modifier = Modifier,
    onProfileClicked:()->Unit = {},
    onBellClicked:()->Unit = {},
    name:String = ""
){
    // Get the current hour of the day
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    // Determine the greeting based on the current hour
    val greeting = when (currentHour) {
        in 0..11 -> "Good Morning" // 00:00 - 11:59
        in 12..15 -> "Good Afternoon" // 12:00 - 15:59
        else -> "Good Evening" // Default case (shouldn't happen, but good for safety)
    }

   // val userDetails by dataStoreManger.getFromDataStore().collectAsState(initial = null)
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                    text = greeting,
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                )
                Spacer(modifier=Modifier.height(1.dp))
                Text(
                    text = name,
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
                modifier = Modifier
                    .size(30.dp)
                    .clickable { onBellClicked() }
            )
        }
    }


@Composable
 fun OnGoingCourses(
    modifier: Modifier = Modifier,
    title: String = "Cyber Security Beginner",
    description:String = "Explore Cybersecurity courses that focus on skills like threat detection, network security, and ...",
    videosLeft:Int = 9
){

    Card(
        modifier = modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFF4776E6), Color(0xFF20408B)),
                    start = Offset(0f, 0f),
                    end = Offset(100f, 1100f)
                ),
            ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent,
            contentColor = Color.White)

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
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

@Preview(showBackground = true)
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
                text = courseName,
                style = TextStyle(
                    fontFamily = urbanist,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

        }
    }
}




@Preview(showBackground = true)
@Composable
fun CourseContent2(
    img2:String = "",
    contentDescription:String = "Fundamentals of Cyber Security",
    courseName:String = "",
    price:String = "",
    noOfStudnets:Int = 4,
    onClick:()->Unit = {},
    subject: String = "14",
    subjectAuthor: String = "None",
    courseId: String = ""
){
    //val ratingResponse = getAverageRatingManually(courseId)
//    Log.i("RAting response","$ratingResponse")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        val painter = rememberAsyncImagePainter(
            model = img2
        )
        Image(
            painter= painter,
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth()
                .height(175.dp)
                .clickable { onClick() }
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(Modifier.height(16.dp))
        TextComm(
            text = courseName,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.88.sp
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
        ){
            EnrolledContent(
                tint = Color.Black,
                showEnroll = false,
                subject = subject,
                subjectAuthor = subjectAuthor,
                rating = " 5"
            )
            Spacer(Modifier.width(20.dp))
            StudentEnrolled(
                noOfStudnets = noOfStudnets
            )

        }
        Spacer(Modifier.height(16.dp))

        val rupeeSymbol = Currency.getInstance(java.util.Locale("en", "IN")).symbol
        ButtonComm(
            text = if(price!="Watch")"Buy at $rupeeSymbol$price" else "Watch",
            onClick = onClick
        )
    }
}

@Composable
fun StudentEnrolled(
    modifier: Modifier = Modifier,
    noOfStudnets:Int = 4
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Icon(
            painter = painterResource(R.drawable.people),
            contentDescription = "Students Icon",
            modifier = Modifier.size(14.dp)

        )
        TextComm(
            fontSize = 12.sp,
            text = "$noOfStudnets"
        )
    }
}


@Composable
fun OnLoading(){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun OnError(
    message:String,
    onClick:()->Unit = {}
){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 18.sp
        )
        ButtonComm(
            text = "Retry",
            onClick = onClick
        )
    }
}
