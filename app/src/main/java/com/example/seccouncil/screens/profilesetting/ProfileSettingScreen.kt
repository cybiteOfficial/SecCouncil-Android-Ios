package com.example.seccouncil.screens.profilesetting

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.seccouncil.R
import com.example.seccouncil.common.ProfileScreenItem
import com.example.seccouncil.common.ProfileScreenSettingItem
import com.example.seccouncil.common.TopAppBar

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileSettingScreen(
    modifier: Modifier = Modifier
) {
    val profilescreensettingitemtitle: List<ProfileSettingItem> =
        listOf(
            ProfileSettingItem(R.drawable.profile, "Name", "Alex Jack son"),
            ProfileSettingItem(R.drawable.mail, "Mail", "alexj1234@gmail.com"),
            ProfileSettingItem(R.drawable.mobile, "Mobile number", "765xxxxxxx"),
            ProfileSettingItem(R.drawable.address, "Address", "123 street,ty colony, xyz road"),
            ProfileSettingItem(R.drawable.postal, "Postal code", "450125")
        )
    TopAppBar(
        title = "Profile",
        content = {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(225.dp)
                    .border(0.5.dp, color = Color.White, RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(R.drawable.profilepic),
                    contentDescription = "profile pic change",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
                Spacer(Modifier.height(5.dp))
                ProfileScreenItem(
                    image = R.drawable.edit_image,
                    title = "Edit profile picture",
                    fontWeight = FontWeight.Thin,
                    color = Color.DarkGray
                    )
                profilescreensettingitemtitle.forEachIndexed{
                index,item->
            ProfileScreenSettingItem(Modifier,item.image,item.title,item.description)
        }}
    )

}

