package com.example.seccouncil.screens.homescreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.seccouncil.MainActivity
import com.example.seccouncil.common.ButtonComposable
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.TopAppBar
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.payment_done
import com.example.seccouncil.payment_gateway.PaymentDetails
import com.example.seccouncil.utlis.DataStoreManger


@Composable
fun ResponsiveCourseDetailScreen(
    navController:NavController,
    courseId: String="",
    onClick: () -> Unit = {},
    paymentDetails: PaymentDetails = PaymentDetails("", "", 0),
//    onBuy: () -> Unit = {},
    profileViewmodel: HomescreenViewmodel,
    email:String = "",
    phone:String = ""
) {
    val context = LocalContext.current
    val dataStoreHelper = remember { DataStoreManger(context) }

    var isPurchased by remember { mutableStateOf(false) }

//    LaunchedEffect(courseId) {
//        isPurchased = dataStoreHelper.isCoursePurchased(courseId)
//    }

    // Fetch course details using API
    val courseDetailResult by profileViewmodel.getCourseDetailResult.collectAsState()

    LaunchedEffect(courseId) {
        profileViewmodel.getCourseDetail(courseId)
    }

    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    var isExpanded1 by remember { mutableStateOf(false) }
    var isExpanded2 by remember { mutableStateOf(false) }

    when (val result = courseDetailResult) {
        is NetworkResponse.Loading -> OnLoading()
        is NetworkResponse.Success -> {

            val courseDetail = result.data.data
            val description = courseDetail.courseDetails.courseDescription
            val whatYouWillLearn = courseDetail.courseDetails.whatYouWillLearn
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val screenWidth = maxWidth
                val screenHeight = maxHeight

                val textSize = (screenWidth / 25).coerceAtLeast(14.dp)  // Ensuring a minimum readable size
                val imageHeight = (screenHeight / 4).coerceAtLeast(120.dp)  // Adjust for smaller screens
                val buttonHeight = (screenHeight / 16).coerceAtLeast(45.dp)

                TopAppBar(
                    title = courseDetail.courseDetails.courseName,
                    onClick = onClick,
                    fontSize = with(density) { textSize.toSp() },
                    iconSize = (screenWidth / 15).coerceAtLeast(22.dp),
                    showTrailingIcon = false,
                    content = {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            val painter = rememberAsyncImagePainter(
                                model = courseDetail.courseDetails.thumbnail
                            )

                            Image(
                                painter = painter,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(imageHeight)
                                    .clip(RoundedCornerShape(10.dp)),
                                contentScale = ContentScale.FillWidth
                            )

                            Spacer(Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                EnrolledContent(tint = Color.Black, showEnroll = false)
                                Spacer(Modifier.width(16.dp))
                                RatingContent()
                            }

                            Spacer(Modifier.height(16.dp))

                            // Description Section
                            ExpandableCard(
                                title = "Description",
                                content = description,
                                isExpanded = isExpanded1,
                                onExpandToggle = { isExpanded1 = !isExpanded1 },
                                maxLines = if (screenWidth < 360.dp) 4 else 6
                            )

                            Spacer(Modifier.height(10.dp))

                            // What You Will Learn Section
                            ExpandableCard(
                                title = "What You Will Learn",
                                content = whatYouWillLearn,
                                isExpanded = isExpanded2,
                                onExpandToggle = { isExpanded2 = !isExpanded2 },
                                maxLines = if (screenWidth < 360.dp) 6 else 9
                            )

                            Spacer(Modifier.height(16.dp))
                            val context = LocalContext.current

                            // Buy Button
                            ButtonComposable(
                                name = courseDetail.courseDetails.price.toString(),
                                startPadding = 0.dp,
                                endPadding = 0.dp,
                                containerColor = Color.Black,
                                contentColor = Color.White,
                                fontSize = with(density) { (textSize * 0.85f).toSp() },
                                height = buttonHeight,
                                fontWeight = FontWeight.SemiBold,
                                onClick = {
                                    if(payment_done){
                                            navController.navigate("onPurchasedScreen")
                                    }else{
                                        (context as? MainActivity)?.startRazorpayPayment(
                                            coursePrice =  courseDetail.courseDetails.price,
                                            email = email,
                                            phone = phone,

                                        )
                                    }
                                }
                            )
                        }
                    }
                )
            }
        }
        is NetworkResponse.Error -> {
            OnError(result.message,onClick={profileViewmodel.getCourseDetail(courseId)})

        }
        null -> OnError("No details found.")
    }


}

@Preview(showSystemUi = true)
@Composable
fun ExpandableCard(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    isExpanded: Boolean,
    onExpandToggle: () -> Unit,
    maxLines: Int,
    verticalPadding: Dp = 10.dp,
    horizontalPadding:Dp = 5.dp
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = modifier.padding(vertical = verticalPadding, horizontal = horizontalPadding)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = if (isExpanded) "Show Less" else "More Details",
                color = Color.Blue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { onExpandToggle() }
                    .padding(vertical = 4.dp)
            )
        }
    }
}
