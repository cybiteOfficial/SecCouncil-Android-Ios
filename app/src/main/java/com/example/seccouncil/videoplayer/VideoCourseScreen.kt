package com.example.seccouncil.videoplayer

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.OptIn
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.seccouncil.R
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.network.getFullCourseDetails.RatingAndReview
import com.example.seccouncil.screens.homescreen.ExpandableCard
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.screens.homescreen.OnError
import com.example.seccouncil.screens.homescreen.OnLoading


@OptIn(UnstableApi::class)
@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ResponsiveCourseDetailScreenOnPurchase(
    homescreenViewmodel: HomescreenViewmodel,
    courseId:String = "",
    videoUri: String = "",
    isFullScreen: Boolean = false,
    onFullScreenToggle: (Boolean) -> Unit = { false },
    viewModel: VideoPlayerViewModel = viewModel(),
    videoItem: VideoItem = VideoItem("", "Getting Started", "", "", 10.3f)
) {

    var videoItemIndex by remember { mutableIntStateOf(0) }

    var isExpanded1 by remember { mutableStateOf(false) }
    val videoItems2 by homescreenViewmodel.videoItems.collectAsState()

    // Fetch course details using API
    val courseDetailResult by homescreenViewmodel.getFullCourseDetailResult.collectAsState()
    var currentVideoUrl by remember { mutableStateOf("https://res.cloudinary.com/dg9mvg2el/video/upload/v1739787392/SecCouncil/x7xqonftu8h9lmeomdlu.mp4") }


// This effect will run when the composable is first launched (only once).
    LaunchedEffect(Unit) {
        // Fetch full course details from the ViewModel using the courseId.
        homescreenViewmodel.getFullCourseDetail(courseId)
        // Log the video items for debugging purposes.
        Log.e("VideoItems", "$videoItems2")
    }



// Get the current context (needed to interact with Android APIs like activities)
    val context = LocalContext.current
// Cast the context to an Activity to manipulate the activity's window
    val activity = context as Activity

// Initialize ExoPlayer (or another video player) when the video URL changes
    LaunchedEffect(currentVideoUrl) {
        // Log the current video URL for debugging purposes.
        Log.e("curr", "$currentVideoUrl")
        // Check if the video URL is not empty, and if so, initialize the player with that URL.
        if (currentVideoUrl.isNotEmpty()) {
            // Call the ViewModel's function to initialize the video player with the new URL
            viewModel.initializePlayer(context, currentVideoUrl)
        }
    }

// Handle full-screen mode and system bars (like the status bar and navigation bar) visibility
    LaunchedEffect(isFullScreen) {
        // Get the window of the current activity
        val window = activity.window

        // If the Android version is 11 (API level 30) or higher, use newer system APIs for hiding/showing bars.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = window.insetsController
            if (isFullScreen) {
                // If full-screen is enabled, hide the system bars (status bar, navigation bar)
                windowInsetsController?.hide(WindowInsets.Type.systemBars())
                windowInsetsController?.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

                // Extend content into the device's notch or cutout area (if available)
                activity.window.attributes.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            } else {
                // If full-screen is not enabled, show the system bars again.
                windowInsetsController?.show(WindowInsets.Type.systemBars())
            }
        } else {
            // For devices with Android version below 11 (API level 30)
            val decorView = activity.window.decorView
            if (isFullScreen) {
                // Hide system bars using an older method (for older Android versions).
                decorView.systemUiVisibility = (
                        View.SYSTEM_UI_FLAG_FULLSCREEN
                                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        )
            } else {
                // Show system bars again (older method).
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            }
        }
    }


    // Release ExoPlayer when this Composable leaves composition
    DisposableEffect(Unit) {
        onDispose {
            viewModel.releasePlayer()
        }
    }

    // Observe ViewModel states
    val exoPlayer = viewModel.exoPlayer
    val isBuffering = viewModel.isBuffering
    Log.e("H2","${courseDetailResult}")
    when (val result = courseDetailResult) {
        is NetworkResponse.Loading -> OnLoading()
        is NetworkResponse.Success ->{
            // calculate rating for the course
            val rating = courseRating(result.data.data.courseDetails.ratingAndReviews.size,result.data.data.courseDetails.ratingAndReviews)
            Column(
                modifier = Modifier
                    .then(
                        if (isFullScreen) {
                            Modifier.fillMaxSize() // Fullscreen: fill entire screen, no padding
                        } else {
                            Modifier
                                .statusBarsPadding()
                                .fillMaxSize()
                                .padding(horizontal = 15.dp) // Normal mode with padding
                        }
                    ),
                horizontalAlignment = Alignment.Start
            ) {
                val videoModifier = if (isFullScreen) {
                    Modifier
                        .fillMaxSize() // Fill the entire screen
                        .background(Color.Black) // Avoid transparent areas
                } else {
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .border(width = 0.5.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                }
                Box(
                    modifier = videoModifier.zIndex(1f)
                ) {
                    // Check if exoPlayer is not null before displaying PlayerView
                    if (exoPlayer != null) {
                        // AndroidView is used to display a view from the Android View system (like a PlayerView in this case) inside a Jetpack Compose UI.
                        AndroidView(
                            // Apply any modifiers to the view to adjust its layout (like padding, size, etc.).
                            modifier = videoModifier,

                            // Factory is used to create the PlayerView when it’s first created (only once).
                            factory = {
                                // Create a new PlayerView (from ExoPlayer) and set up its properties.
                                PlayerView(context).apply {
                                    // Set the ExoPlayer instance to the PlayerView, so the video can be played.
                                    player = exoPlayer
                                    // Enable the video controller (like play, pause buttons) to be shown.
                                    useController = true
                                    // Set how the video should scale to fit the view. RESIZE_MODE_FILL will stretch the video to fill the available space.
                                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
                                }
                            },

                            // Update block is called when the PlayerView is recomposed. We are updating the player to make sure it has the current ExoPlayer instance.
                            update = {
                                // Ensure the player is updated with the latest ExoPlayer instance (in case it has changed).
                                it.player = exoPlayer
                            }
                        )
                    } else {
                        // Optionally, show a loading spinner or placeholder while player is initializing
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center),
                            color = Color.White
                        )
                    }
                    // Show loading indicator while buffering
                    if (isBuffering) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center),
                            color = Color.White
                        )
                    }
                    val IconModifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(if (isFullScreen) 30.dp else 16.dp)
                    // Fullscreen Toggle Button
                    IconButton(
                        onClick = {
                            if (isFullScreen) {
                                // Exit fullscreen mode (Portrait)
                                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                            } else {
                                // Enter fullscreen mode (Landscape)
                                activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                            }
                            onFullScreenToggle(!isFullScreen)
                        },
                        modifier = IconModifier
                            .background(Color.Black.copy(alpha = 0.7f), shape = CircleShape)
                    ) {
                        Icon(
                            painter = painterResource(
                                if (isFullScreen) R.drawable.baseline_fullscreen_exit_24
                                else R.drawable.baseline_fullscreen_24
                            ),
                            contentDescription = "Toggle Fullscreen",
                            tint = Color.White
                        )
                    }

// Use LaunchedEffect to toggle orientation
//                    LaunchedEffect(isFullScreen) {
//                        val activity = context as Activity
//                        if (isFullScreen) {
//                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
//                        } else {
//                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                        }
      //              }

                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    EnrolledContent(
                        tint = Color.Black, showEnroll = false,
                        subject = result.data.data.courseDetails.courseContent.size.toString(),
                        subjectTime = result.data.data.totalDuration.toString()
                    )
                    Spacer(Modifier.width(8.dp))
                    RatingContent(
                        courseRating = rating,
                        totalGivenRating = result.data.data.courseDetails.ratingAndReviews.size.toString()
                    )
                }
                Spacer(Modifier.height(8.dp))

                ExpandableCard(
                    modifier = Modifier.fillMaxWidth(),
                    title = "Description",
                    content = videoItem.description,
                    isExpanded = isExpanded1,
                    onExpandToggle = { isExpanded1 = !isExpanded1 },
                    maxLines = 4
                )

                Spacer(Modifier.height(16.dp))

                LazyColumn(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items = videoItems2) { index, item ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (videoItemIndex != index && item.videoUrl.isNotEmpty()) {
                                        videoItemIndex = index
                                        currentVideoUrl = item.videoUrl
                                        viewModel.releasePlayer()
                                        viewModel.initializePlayer(context, item.videoUrl)
                                    }
                                    Log.e("H3", item.videoUrl)
                                    Log.e("curr","Clicked")
                                    Log.e("curr","$currentVideoUrl")


//
//                                    // Ensure the player is initialized with the new video
//                                    viewModel.initializePlayer(context, item.videoUrl)
                                }
                        ) {
                            ResponsiveCourseDetailContent(
                                index = index + 1,
                                videoItem = VideoItem(
                                    videoUrl = item.videoUrl,
                                    title = item.title,
                                    description = item.description,
                                    videoId = "1",
                                    durationSeconds = item.timeDuration.toFloat()
                                    )
                            )
                        }
                    }
                }
            }
        }
        is NetworkResponse.Error -> {
            OnError(message = result.message)

        }
        null -> {
            OnError("No details found.")
        }
}
}


@Preview(showBackground = true)
@Composable
fun ResponsiveCourseDetailContent(
    index: Int = 1,
    textSize: Dp = 16.dp,
    videoItem: VideoItem = VideoItem("", "Getting Started", "", "", 10.36f)
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = 1.dp,
                color = Color.DarkGray,
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextComm(
                // Convert DP to sp for text size
                text = "$index.  ${videoItem.title}",
                fontSize = with(LocalDensity.current) { textSize.toSp() },
                lineHeight = with(LocalDensity.current) { textSize.toSp() }
            )
            Spacer(Modifier.weight(1f))
            TextComm(
                text = "${videoItem.durationSeconds.formatToTwoDecimalPlaces()} Mins",
                fontSize = with(LocalDensity.current) { (textSize * 0.8f).toSp() },
                lineHeight = with(LocalDensity.current) { textSize.toSp() },
                color = Color.Gray
            )
        }

    }
}

// Extension function to format Double to two decimal places without rounding
fun Float.formatToTwoDecimalPlaces(): String {
    val truncatedValue = (this * 100).toInt() / 100.0
    return String.format("%.2f", truncatedValue)
}
fun Float.formatToOneDecimalPlaces(): String {
    val truncatedValue = (this * 100).toInt() / 100.0
    return String.format("%.1f", truncatedValue)
}

private fun courseRating(totalRating: Int, rating: List<RatingAndReview>): String{
var ratingSum = 0
    for (rat in rating){
        ratingSum+=rat.rating
    }
    val finalrating = (ratingSum/totalRating.toFloat()).formatToOneDecimalPlaces()
return finalrating.toString()
}