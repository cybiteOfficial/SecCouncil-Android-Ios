package com.example.seccouncil.screens.profilesetting

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seccouncil.R
import com.example.seccouncil.common.ProfileScreenItem
import com.example.seccouncil.common.TopAppBar

@Preview(showSystemUi = true)
@Composable
fun HelpCenter(
    onBackClicked:()->Unit ={}
){
    TopAppBar(
        title = "Help Center",
        modifier = Modifier,
        content = { HelpCenterScreen() },
        onClick = onBackClicked
    )
}



@Composable
private fun HelpCenterScreen(
    modifier: Modifier=Modifier
){
    ProfileScreenItem(
        title = "Chat with us?",
        modifier = Modifier.padding(top = 20.dp),
        image = R.drawable.chatbot
    )
}
