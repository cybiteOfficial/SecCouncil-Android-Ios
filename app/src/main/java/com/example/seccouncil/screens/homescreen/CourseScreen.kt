package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seccouncil.common.ButtonComm
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.network.getAllCourseDetailsModel.Data
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.network.getEnrolledCourse.EnrolledCourse

@Preview(showSystemUi = true)
@Composable
fun CourseScreen(
    modifier: Modifier = Modifier,
    onEnrollClicked:()->Unit = {},
    onBackClicked:()->Unit = {},
    viewModel: HomescreenViewmodel,
    navController:NavController
){
    val courses by viewModel.enrolledCourses.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.getEnrolledCourse()
//    }

    val totalEnrolledCourse = courses
    Column(
        modifier = Modifier
            .safeDrawingPadding()
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ){
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                OnGoingCourses()

            when (courses) {
                is NetworkResponse.Loading -> {
                    CircularProgressIndicator()
                }

                is NetworkResponse.Success -> {
                    val enrolledCourses = (courses as NetworkResponse.Success<EnrolledCourse>).data.data
                    if (enrolledCourses.isEmpty()) {
                        Text(
                            text = "No Course Enrolled",
                            fontSize = 20.sp,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    } else {
                        LazyColumn {
                            items(enrolledCourses) { course ->
                                CourseContent2(
                                    img2 = course.thumbnail,
                                    courseName = course.courseName,
                                    price = "Watch",
                                    noOfStudnets = course.studentsEnrolled.size,
                                    onClick = {
                                        navController.navigate("onPurchasedScreen/${course._id}")
                                    }
                                )
                            }
                        }
                    }
                }

                is NetworkResponse.Error -> {
                    Text(
                        text = (courses as NetworkResponse.Error).message,
                        color = Color.Red
                    )
                }

                null -> {
                    Text(
                        text = "Loading courses...",
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
fun CourseContent(
    img2:String = "",
    contentDescription:String = "Fundamentals of Cyber Security",
    courseName:String = "",
    price:String = ""
){
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
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ){
        EnrolledContent(
            tint = Color.Black,
            showEnroll = false
        )
        Spacer(Modifier.width(20.dp))
        RatingContent()
    }
    Spacer(Modifier.height(16.dp))
    ButtonComm(
        text = price
    )
}
}

@Composable
private fun OnSuccessScreen(
    modifier: Modifier = Modifier,
    courseList:List<Data>
){
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.Start
    ) {
        items(courseList){
            course->
            CourseContent(
                img2 = course.thumbnail,
                courseName = course.courseName,
                price = course.price.toString()
            )
        }
    }
}

@Composable
private fun OnLoadingScreen(){
    Column(
            modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            CircularProgressIndicator()
    }
}

@Composable
private fun OnErrorScreen(
    message:String
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
    }
}
