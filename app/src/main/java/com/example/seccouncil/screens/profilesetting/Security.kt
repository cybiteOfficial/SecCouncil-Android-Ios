package com.example.seccouncil.screens.profilesetting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seccouncil.R
import com.example.seccouncil.common.ProfileScreenItem
import com.example.seccouncil.common.TopAppBar

@Preview(showSystemUi = true)
@Composable
fun Security(
    onBackClicked:()->Unit ={}
){
        TopAppBar(
            title = "Security",
            modifier = Modifier,
            content = { SecurityScreen() },
            onClick = onBackClicked
        )
}

@Composable
private fun SecurityScreen(
    modifier: Modifier = Modifier
){
    val securityItems = listOf("Old Password","New Password","Confirm Password")
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(top = 20.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        securityItems.forEach{
            item->ProfileScreenItem(
            title = item,
            color = Color.Gray,
            image = R.drawable.password
        )
        }

    }
}