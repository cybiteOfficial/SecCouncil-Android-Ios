package com.example.seccouncil.videoplayer

import android.app.Activity
import android.content.pm.ActivityInfo
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
import com.example.seccouncil.screens.homescreen.ExpandableCard
import com.google.android.exoplayer2.ui.PlayerView


@Preview(showSystemUi = true)
@Composable
fun ResponsiveCourseDetailScreenOnPurchase(
    videoUri: String = "",
    isFullScreen: Boolean = false,
    onFullScreenToggle: (Boolean) -> Unit = {false},
    viewModel: VideoPlayerViewModel = viewModel(),
    videoItem: VideoItem = VideoItem("", "Getting Started", "", "", 10.3f)
) {

    var videoItemIndex by remember { mutableIntStateOf(0) }

    var isExpanded1 by remember { mutableStateOf(false) }


    val context = LocalContext.current
    val activity = context as Activity

    // Initialize ExoPlayer when videoUri changes
    LaunchedEffect(videoUri) {
        viewModel.initializePlayer(context, videoUri)
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



    Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.Start
        ) {
        val videoModifier = if (isFullScreen) {
            Modifier.fillMaxSize() // Fullscreen mode
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
            // Set video layout based on fullscreen state
            // Use AndroidView to embed PlayerView
            if (exoPlayer != null) {
                AndroidView(
                    modifier = videoModifier,
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = true
                        }
                    }
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
            }else{
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            }

            // Fullscreen Toggle Button
            IconButton(
                onClick = {
                    if (isFullScreen) {
                        // Exit fullscreen mode (Portrait)
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    } else {
                        // Enter fullscreen mode (Landscape)
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
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
                itemsIndexed(items = videoItems) { index, item ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (videoItemIndex != index) {
                                    viewModel.releasePlayer()
                                    videoItemIndex = index
                                }
                            }
                    ) {
                        ResponsiveCourseDetailContent()
                    }
                }
            }
        }
}


@Preview(showBackground = true)
@Composable
fun ResponsiveCourseDetailContent(
    index: Int = 1,
    textSize: Dp = 16.dp,
    videoItem:VideoItem = VideoItem("","Getting Started","","",10.3f)
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
    ){
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