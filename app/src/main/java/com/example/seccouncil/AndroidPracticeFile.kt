package com.example.seccouncil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import java.util.Timer
import kotlin.concurrent.schedule


@Preview(showSystemUi = true)
@Composable
fun TimerScreen() {
    var time by remember { mutableStateOf(0) }

    DisposableEffect(Unit) {
        val timer = Timer().schedule(0,1000){
            time++
        }
        onDispose { timer.cancel() }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Elapsed time: ${time} seconds")
    }
}