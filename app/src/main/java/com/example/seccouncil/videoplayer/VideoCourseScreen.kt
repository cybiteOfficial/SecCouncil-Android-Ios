package com.example.seccouncil.videoplayer

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
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
import com.example.seccouncil.R
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.network.getAllCourseDetailsModel.NetworkResponse
import com.example.seccouncil.screens.homescreen.ExpandableCard
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.screens.homescreen.OnError
import com.example.seccouncil.screens.homescreen.OnLoading
import com.google.android.exoplayer2.ui.PlayerView
import kotlinx.coroutines.delay


@Preview(showSystemUi = true)
@Composable
fun ResponsiveCourseDetailScreenOnPurchase(
    homescreenViewmodel: HomescreenViewmodel,
    courseId:String,
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
    var currentVideoUrl by remember { mutableStateOf("https://res.cloudinary.com/dg9mvg2el/video/upload/v1734429828/SecCouncil/ikufkzuepw8l28cxv0li.mp4") }


    LaunchedEffect(Unit) {
        homescreenViewmodel.getFullCourseDetail(courseId)
        Log.e("VideoItems", "$videoItems2")
    }



    val context = LocalContext.current
    val activity = context as Activity

    // Initialize ExoPlayer when videoUri changes
    LaunchedEffect(currentVideoUrl) {
//        Log.e("curr","$currentVideoUrl")
//        if (currentVideoUrl.isNotEmpty()) {
//            viewModel.releasePlayer()
//            Handler(Looper.getMainLooper()).postDelayed({
//                viewModel.initializePlayer(context, currentVideoUrl)
//            }, 300)
//           // viewModel.initializePlayer(context, currentVideoUrl)
//        }
        Log.e("curr", "$currentVideoUrl")
        if (currentVideoUrl.isNotEmpty()) {
            viewModel.initializePlayer(context, currentVideoUrl)
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
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .fillMaxSize()
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.Start
            ) {
                val videoModifier = if (isFullScreen) {
                    Modifier
                        .fillMaxSize() // Fullscreen mode
                        .aspectRatio(16f / 9f) // Forces 16:9 aspect ratio to match most videos
                } else {
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                }
                Box(
                    modifier = videoModifier
                        .border(width = 0.5.dp, color = Color.White, shape = RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black)
                        .zIndex(1f)
                ) {
                    // Check if exoPlayer is not null before displaying PlayerView
                    if (exoPlayer != null) {
                        AndroidView(
                            modifier = videoModifier,
                            factory = {
                                PlayerView(context).apply {
                                    player = exoPlayer
                                    useController = true
                                }
                            },
                            update = { it.player = exoPlayer }
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
                    val IconModifier = if (isFullScreen) {
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 30.dp, end = 15.dp)
                    } else {
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(16.dp)
                    }
                    // Fullscreen Toggle Button
                    IconButton(
                        onClick = {
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
                    LaunchedEffect(isFullScreen) {
                        val activity = context as Activity
                        if (isFullScreen) {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        } else {
                            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        }
                    }

                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    EnrolledContent(tint = Color.Black, showEnroll = false)
                    Spacer(Modifier.width(8.dp))
                    RatingContent()
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
    videoItem: VideoItem = VideoItem("", "Getting Started", "", "", 10.3f)
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
                .padding(horizontal = 16.dp, vertical = 4.dp),
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
                text = "${videoItem.durationSeconds} Mins",
                fontSize = with(LocalDensity.current) { (textSize * 0.8f).toSp() },
                lineHeight = with(LocalDensity.current) { textSize.toSp() },
                color = Color.Gray
            )
            Spacer(Modifier.width(4.dp))
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.download),
                    contentDescription = "Download"
                )
            }
        }
    }
}