package com.example.seccouncil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import coil3.compose.AsyncImage
import com.example.seccouncil.navigation.Navigation
import com.example.seccouncil.screens.SignUp
import com.example.seccouncil.ui.theme.SecCouncilTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Install the splash screen
        installSplashScreen().setKeepOnScreenCondition {
            false // Allows the system to immediately dismiss the splash screen
        }
//        // Set content
//        setContent {
//            SplashScreen()
//        }

        enableEdgeToEdge()
        setContent {
            SecCouncilTheme {
                Navigation()
            }
        }
    }
}
