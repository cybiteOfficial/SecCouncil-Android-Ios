package com.example.seccouncil.screens.profilesetting

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.seccouncil.R
import com.example.seccouncil.common.ProfileImage
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.ui.theme.urbanist
import com.example.seccouncil.utlis.DataStoreManger
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit = {},
    onBackClicked: () -> Unit = {},
    name: String = "",
    email: String = "",
    viewModel: HomescreenViewmodel = HomescreenViewmodel(
        context = LocalContext.current,
        dataStoreManager = DataStoreManger(LocalContext.current)
    ),
    onLogoutClicked: () -> Unit = {},
    dob: String,
    gender: String,
    phoneNumber: String,
    about: String

){
    val context = LocalContext.current

    // Collect the current file name from the ViewModel’s StateFlow
    val imageFileName by viewModel.imageFileName.collectAsState()
    val imageUrl = ProfileImage.profileImage?.collectAsState(null)
    Log.d("ProfileImage_","$imageUrl")
    LaunchedEffect(imageUrl) {
        // Logs only once per URL change
        Log.d("ImageLoad", "Loading URL: $imageUrl")
    }

    // This checks whether the image file name is still being fetched.
    // For example, if you want a loading indicator while the file name is null from DataStore:
    val isLoading = (imageFileName == null)

    Log.e("ProfileImage", "Value: $imageFileName")
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White, // Set the background color of the TopAppBar
                    titleContentColor = Color.Black, // Color for the title
                    actionIconContentColor = Color.Black, // Color for the action icons like bell and profile
                    navigationIconContentColor = Color.Black // Color for the navigation icon, if any
                ),
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                    ){
                        Spacer(Modifier.weight(0.4f))
                        Text(
                            text = "Profile",
                            style = TextStyle(
                                fontFamily = urbanist,
                                fontSize = 20.sp)
                        )
                        Spacer(Modifier.weight(0.6f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) {
        innerPadding->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 15.dp)
            ,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = modifier.fillMaxWidth()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .border(0.5.dp, color = Color.White, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                ) {

                    if (!imageFileName.isNullOrEmpty()) {
                        Log.e("Branch", "Using local image")
                        // Display the image saved to internal storage
                        if (isLoading) {
                            // Show spinner if still loading from DataStore
                            CircularProgressIndicator(Modifier.align(Alignment.Center))
                        } else {
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
                        }
                    } else if ((imageUrl?.value?.photo?.isNullOrEmpty() == false)) {
                        Log.e(
                            "ProfileImage : ",
                            "${ProfileImage.profileImage}  2"
                        )

                        val request = ImageRequest.Builder(context)
                            .data(imageUrl.value!!.photo)
                            .crossfade(true)
                            .placeholder(R.drawable.profilepic)
                            .error(R.drawable.profilepic)
                            .build()

                        val painter = rememberAsyncImagePainter(model = request)
                        if (painter.state is AsyncImagePainter.State.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(100.dp),
                                color = Color.Gray,
                                strokeWidth = 8.dp
                            )
                        }
                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .size(225.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )

                    }
                    else {
                        Image(
                            painter = painterResource(R.drawable.profilepic),
                            contentDescription = "Default",
                            modifier = Modifier
                                .size(225.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
            }
        }
                Spacer(Modifier.height(5.dp))
                Text(
                    text = name,
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = email,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 16.sp,
                    color = Color.Gray

                )
            }
            ProfileContent2(
                dob = dob,
                gender = gender,
                about = about,
                phone = phoneNumber
            )
            Spacer(Modifier.height(5.dp))
           Button(
               onClick = onProfileClicked,
               modifier = Modifier
                   .fillMaxWidth(0.5f),
               colors = ButtonColors(
                   containerColor = Color.Black,
                   disabledContentColor = Color.White,
                   contentColor = Color.White,
                   disabledContainerColor = Color.Black
               ),
               shape = RoundedCornerShape(10.dp)

           ) {
               Text(
                   "Edit Profile",
                   style = TextStyle(
                       fontFamily = urbanist
                   )
               )
           }
            Button(
                onClick = onLogoutClicked,
                modifier = Modifier
                    .fillMaxWidth(0.5f),
                colors = ButtonColors(
                    containerColor = Color.Red,
                    disabledContentColor = Color.White,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Black
                ),
                shape = RoundedCornerShape(10.dp)

            ) {
                Text(
                    "Logout",
                    style = TextStyle(
                        fontFamily = urbanist
                    )
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun ProfileContent2(
    dob: String ,
    gender: String,
    phone: String ,
    about: String ,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val isCompact = screenWidth <= 360

    val padding = if (isCompact) 4.dp else 6.dp
    val labelFontSize = if (isCompact) 12.sp else 14.sp

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(horizontal = padding)) {
            ProfileInfoItem(
                label = "Date of Birth",
                value = if(dob=="")"Upload your DOB" else dob,
            )
            ProfileInfoItem(
                label = "Gender",
                value = if(gender=="")"Select Gender" else gender,
            )
            ProfileInfoItem(
                label = "Phone Number",
                value = if(phone=="")"Add Phone Number" else phone,
            )
            Text(
                text = "About",
                fontSize = labelFontSize,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(vertical = padding),
                fontFamily = urbanist
            )
            OutlinedReadonlyTextField(
                value = if(about=="")"Tell us about yourself" else about

            )
        }
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val isCompact = screenWidth <= 360

    // Dynamic sizes
    val labelFontSize = if (isCompact) 12.sp else 14.sp
    val valueFontSize = if (isCompact) 14.sp else 16.sp
    val verticalPadding = if (isCompact) 4.dp else 6.dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = verticalPadding)
    ) {
        Text(
            text = label,
            fontSize = labelFontSize,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = labelFontSize),
            color = MaterialTheme.colorScheme.primary,
            fontFamily = urbanist
        )
        Text(
            text = value,
            fontSize = valueFontSize,
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = valueFontSize),
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = urbanist
        )
    }
}

@Composable
fun OutlinedReadonlyTextField(
    value: String,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val isCompact = screenWidth <= 360

    // Responsive dimensions
    val boxHeight = if (isCompact) 80.dp else 100.dp
    val contentPadding = if (isCompact) 6.dp else 8.dp
    val fontSize = if (isCompact) 14.sp else 16.sp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(boxHeight)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(contentPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
        ) {
            Text(
                text = value,
                fontSize = fontSize,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = urbanist
                )
            )
        }
    }
}