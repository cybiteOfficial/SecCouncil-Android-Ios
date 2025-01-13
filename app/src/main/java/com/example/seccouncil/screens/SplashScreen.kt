package com.example.seccouncil.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.example.seccouncil.R
import kotlinx.coroutines.delay


@Preview(showBackground = true)
@Composable
fun SplashScreen(onAnimationComplete: () -> Unit = {}) {
    // Use Coil's AsyncImage to load the GIF from drawable resources
    LaunchedEffect(Unit) {
        delay(3000) // Show splash screen for 3 seconds
        onAnimationComplete() // Trigger navigation to the next screen
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = R.drawable.logo3,
            contentDescription = "Splash Animation",
            modifier = Modifier.fillMaxSize()
        )
    }
}
