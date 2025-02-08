package com.example.seccouncil.screens.profilesetting

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.seccouncil.R
import com.example.seccouncil.common.ProfileScreenSettingItem
import com.example.seccouncil.common.TopAppBar
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.ui.theme.urbanist
import kotlinx.coroutines.CoroutineScope
import java.io.File

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileSettingScreen(
    modifier: Modifier = Modifier,
    onBackClicked:()->Unit ={},
    name:String = "",
    email:String = "",
    phoneNumber:String = "",
    viewModel: HomescreenViewmodel,
    scope: CoroutineScope
) {
    val profilescreensettingitemtitle: List<ProfileSettingItem> =
        listOf(
            ProfileSettingItem(R.drawable.profile, "Name", name),
            ProfileSettingItem(R.drawable.mail, "Mail", email),
            ProfileSettingItem(R.drawable.mobile, "Mobile number", phoneNumber),
        )

    val context = LocalContext.current

    // Collect the current file name from the ViewModel’s StateFlow
    val imageFileName by viewModel.imageFileName.collectAsState()

    // This checks whether the image file name is still being fetched.
    // For example, if you want a loading indicator while the file name is null from DataStore:
    val isLoading = (imageFileName == null)

    // Launcher for picking images
    val getContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.saveImageToInternalStorage(it)
        }
    }



    TopAppBar(
        title = "Profile",
        onClick = onBackClicked,
        content = {
            Column(
                modifier = modifier
                    //.verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(225.dp)
                        .border(0.5.dp, color = Color.White, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    if (isLoading) {
                        // Show spinner if still loading from DataStore
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    } else if (!imageFileName.isNullOrEmpty()) {
                        // Display the image saved to internal storage
                        Image(
                            painter = rememberAsyncImagePainter(
                                File(context.filesDir, imageFileName!!)
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(225.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Show a default image if no filename stored
                        Image(
                            painter = painterResource(R.drawable.profilepic),
                            contentDescription = "Default",
                            modifier = Modifier
                                .size(225.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.gallary2), // Replace with your gallery icon
                        contentDescription = "Change Profile Picture",
                        tint = Color.Black, // Adjust icon color as needed
                        modifier = Modifier
                            .size(56.dp) // Adjust icon size as needed
                            .align(Alignment.BottomEnd) // Align to bottom-right
                            .padding(8.dp) // Add padding for spacing
                            .background(Color.White, CircleShape) // Add background to the icon
                            .padding(8.dp)
                            .clickable {
                                getContentLauncher.launch("image/*")
                            }
                    )
                }
                Spacer(Modifier.height(5.dp))
                profilescreensettingitemtitle.forEachIndexed { index, item ->
                    ProfileScreenSettingItem(Modifier, item.image, item.title, item.description)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(width = 160.dp, height = 40.dp),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Verify OTP",
                        style = TextStyle(
                            fontFamily = urbanist,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(Modifier.weight(1f))
                Text(
                    text = "Save Changes",
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textMotion = TextMotion.Animated
                    ),
                    modifier = Modifier
                        .clickable {
                        }
                )
            }
        }
    )

}

