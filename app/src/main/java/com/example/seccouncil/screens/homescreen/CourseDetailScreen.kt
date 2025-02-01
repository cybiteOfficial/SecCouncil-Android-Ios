package com.example.seccouncil.screens.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.ButtonComposable
import com.example.seccouncil.common.EnrolledContent
import com.example.seccouncil.common.RatingContent
import com.example.seccouncil.common.TextComm
import com.example.seccouncil.common.TopAppBar


@Preview(showSystemUi = true)
@Composable
fun ResponsiveCourseDetailScreen(onClick: () -> Unit = {}) {
    // BoxWithConstraints to get the maxWidth and maxHeight of the composable
    BoxWithConstraints {
        // Convert constraints to DP using LocalDensity if needed
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        // Example: set a base dimension and scale it according to screen width
        val textSize = (screenWidth / 25).coerceAtLeast(14.dp)  // Minimum 14.dp
        val imageHeight = (screenHeight / 5).coerceAtLeast(120.dp)

        TopAppBar(
            title = "Fundamentals of Cyber Security",
            onClick = onClick,
            // Use a scaled sp value here:
            fontSize = with(LocalDensity.current) { textSize.toSp() },
            iconSize = (screenWidth / 15).coerceAtLeast(24.dp),
            showTrailingIcon = false,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Image(
                        painter = painterResource(R.drawable.frame_32),
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
                        EnrolledContent(
                            tint = Color.Black,
                            showEnroll = false
                        )
                        Spacer(Modifier.width(20.dp))
                        RatingContent()
                    }

                    Spacer(Modifier.height(24.dp))

                    ButtonComposable(
                        name = "Buy 25,000 /-",
                        startPadding = 0.dp,
                        endPadding = 0.dp,
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        // Scale the font size inside the button
                        fontSize = with(LocalDensity.current) { (textSize * 0.8f).toSp() },
                        height = (screenHeight / 20).coerceAtLeast(35.dp),
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.height(35.dp))

                    // Dynamically generated course detail items
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        repeat(7) {
                            ResponsiveCourseDetailContent(textSize = textSize)
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ResponsiveCourseDetailContent(
    id: Int = 1,
    title: String = "Getting Started",
    time: Int = 10,
    textSize: Dp = 16.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(10.dp),
                ambientColor = Color.LightGray,
                spotColor = Color.LightGray,
                clip = false
            )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextComm(
                // Convert DP to sp for text size
                text = "$id.  $title",
                fontSize = with(LocalDensity.current) { textSize.toSp() },
                lineHeight = with(LocalDensity.current) { textSize.toSp() }
            )
            Spacer(Modifier.weight(1f))
            TextComm(
                text = "$time Mins",
                fontSize = with(LocalDensity.current) { (textSize * 0.8f).toSp() },
                lineHeight = with(LocalDensity.current) { textSize.toSp() },
                color = Color.Gray
            )
        }
    }
}