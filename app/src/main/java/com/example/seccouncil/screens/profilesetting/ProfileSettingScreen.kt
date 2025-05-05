package com.example.seccouncil.screens.profilesetting

import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.seccouncil.R
import com.example.seccouncil.common.DateUtils
import com.example.seccouncil.common.ProfileImage
import com.example.seccouncil.common.TopAppBar
import com.example.seccouncil.screens.homescreen.HomescreenViewmodel
import com.example.seccouncil.ui.theme.urbanist
import java.io.File

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun ProfileSettingScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    viewModel: HomescreenViewmodel
) {

    val context = LocalContext.current
    val uploadStatus by viewModel.uploadStatus.collectAsState()
    val tempImageUri by viewModel.tempImageUri.collectAsState()
    val isUploading by viewModel.isUploading.collectAsState()
    val profileCloudImage = ProfileImage?.profileImage?.collectAsState(null)
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf("DD/MM/YYYY") }
    var selectedGender by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Male", "Female", "Other")

    var selectedCountryCode by remember { mutableStateOf("+91") }
    var countryCodeExpanded by remember { mutableStateOf(false) }
    val countryCodes = listOf("+91", "+1", "+44", "+81")

    var selectedNumber by remember {mutableStateOf("") }
    var selectedAbout by remember { mutableStateOf("") }

    Log.e(
        "ProfileDebug", """
    First Name: $firstName
    Last Name: $lastName
    Gender: $selectedGender
    DOB: $selectedDate
    Country Code: $selectedCountryCode
    Phone Number: ${selectedNumber.toLongOrNull() ?: 0L}
    About: $selectedAbout
""".trimIndent()
    )
// Show Toast when uploadStatus changes

    LaunchedEffect(uploadStatus) {
        uploadStatus?.let { status ->
            Toast.makeText(context, status, Toast.LENGTH_SHORT).show()
            Log.e("ProfileDebug", "Toast is $status")

            if (status.contains("success", ignoreCase = true)) {
                // Clear all fields after successful upload
                firstName = ""
                lastName = ""
                selectedNumber = ""
                selectedGender = ""
                selectedDate = ""
                selectedAbout = ""
                viewModel.setTempImageUri(null)
            }

            // Clear status once toast and logic is done
            viewModel.clearUploadStatus()
        }
    }
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
            viewModel.setTempImageUri(it) // Cache the picked image URI
        }
    }

    TopAppBar(
        showTrailingIcon = false,
        title = "Profile",
        onClick = onBackClicked,
        content = {
            Column(
                modifier = modifier
                    //.verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(225.dp)
                        .border(0.5.dp, color = Color.White, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center // This centers the loader
                ) {
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                    when {
                        tempImageUri != null -> {
                            // Show the selected image (not yet uploaded)
                            Image(
                                painter = rememberAsyncImagePainter(tempImageUri),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(225.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        !imageFileName.isNullOrEmpty() -> {
                            // Show saved image from internal storage
                            if (isLoading) {
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            } else {
                                Image(
                                    painter = rememberAsyncImagePainter(
                                        File(
                                            context.filesDir,
                                            imageFileName!!
                                        )
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(225.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }

                        (profileCloudImage?.value?.photo.isNullOrEmpty() == false) -> {
                            val request = ImageRequest.Builder(context)
                                .data(profileCloudImage.value!!.photo)
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

                        else -> {
                            // Show default profile picture
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
                    // Show loading indicator on top of the image
                    if (isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(48.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Icon(
                        painter = painterResource(R.drawable.add_photo_alternate), // Replace with your gallery icon
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
                ProfileNewContent(
                    firstName = firstName,
                    onFirstNameChange = { firstName = it },
                    lastName = lastName,
                    onLastNameChange = { lastName = it },

                    selectedGender = selectedGender,
                    genderOptions = genderOptions,
                    genderExpanded = genderExpanded,
                    onGenderExpandedChange = { genderExpanded = it },
                    onGenderSelected = { selectedGender = it },

                    selectedCountryCode = selectedCountryCode,
                    onCountryCodeChange = { selectedCountryCode = it },
                    countryCodeExpanded = countryCodeExpanded,
                    onCountryCodeExpandedChange = { countryCodeExpanded = it },
                    countryCodes = countryCodes,

                    selectedNumber = selectedNumber,
                    onNumberChange = { selectedNumber = it },

                    selectedAbout = selectedAbout,
                    onAboutChange = { selectedAbout = it },
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it }
                )
                Text(
                    text = "Save Changes",
                    style = TextStyle(
                        fontFamily = urbanist,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        textMotion = TextMotion.Animated
                    ),
                    modifier = Modifier
                        .clickable(enabled = !isUploading) { // Disable button while uploading git commit --amend -m "New commit message"
//                            tempImageUri?.let { uri ->
//                                viewModel.saveImageToInternalStorage(uri)
//                            }
//                            viewModel.updateProfile(
//                                firstName = firstName,
//                                lastName =  lastName,
//                                contactNumber = selectedNumber.toLongOrNull() ?: 0L,
//                                about = selectedAbout,
//                                dateOfBirth = selectedDate,
//                                gender = selectedGender
//                            )
                            viewModel.updateUserProfile(
                                firstName = firstName,
                                lastName = lastName,
                                contactNumber = selectedNumber.toLongOrNull() ?: 0L,
                                about = selectedAbout,
                                dob = selectedDate,
                                gender = selectedGender,
                                imageUri = tempImageUri
                            )
                        }
                )
                Spacer(Modifier.weight(1f))
            }

        }
    )
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true)
@Composable
private fun ProfileNewContent(
    firstName: String,
    onFirstNameChange: (String) -> Unit,

    lastName: String,
    onLastNameChange: (String) -> Unit,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    selectedGender: String,
    genderOptions: List<String>,
    genderExpanded: Boolean,
    onGenderExpandedChange: (Boolean) -> Unit,
    onGenderSelected: (String) -> Unit,

    selectedCountryCode: String,
    onCountryCodeChange: (String) -> Unit,
    countryCodeExpanded: Boolean,
    onCountryCodeExpandedChange: (Boolean) -> Unit,
    countryCodes: List<String>,

    selectedNumber: String,
    onNumberChange: (String) -> Unit,

    selectedAbout: String,
    onAboutChange: (String) -> Unit

) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val spacing = if (screenWidth <= 360) 8.dp else 12.dp
    val labelFontSize = if (screenWidth <= 360) 12.sp else 14.sp


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacing, vertical = 8.dp)
            .imePadding()
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = onFirstNameChange,
                modifier = Modifier.weight(1f),
                label = { Text("First Name", fontSize = labelFontSize) },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = urbanist
                )
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = onLastNameChange,
                modifier = Modifier.weight(1f),
                label = { Text("Last Name", fontSize = labelFontSize) },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = urbanist
                )
            )
        }
        Spacer(modifier = Modifier.height(spacing))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            DatePickerWithDialog(
                modifier = Modifier.weight(1f),
                selectedDate = selectedDate,
                onDateSelected = { newDate -> onDateSelected(newDate) }
            )
            Box(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onGenderExpandedChange(true) },
                    label = { Text("Gender", fontSize = labelFontSize) },
                    trailingIcon = {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            modifier = Modifier.clickable { onGenderExpandedChange(true) }
                        )
                    },
                    textStyle = TextStyle(
                        fontFamily = urbanist
                    )
                )
                DropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { onGenderExpandedChange(false) },
                    containerColor = Color.White
                ) {
                    genderOptions.forEach { gender ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = gender,
                                    fontFamily = urbanist
                                )
                            },
                            onClick = {
                                onGenderSelected(gender)
                                onGenderExpandedChange(false)
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(spacing))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            ExposedDropdownMenuBox(
                expanded = countryCodeExpanded,
                onExpandedChange = onCountryCodeExpandedChange
            ) {
                OutlinedTextField(
                    value = selectedCountryCode,
                    onValueChange = onCountryCodeChange,
                    readOnly = true,
                    label = { Text("Code", fontSize = labelFontSize) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryCodeExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor()
                        .width(100.dp)
                )

                ExposedDropdownMenu(
                    expanded = countryCodeExpanded,
                    onDismissRequest = { onCountryCodeExpandedChange(false) }
                ) {
                    countryCodes.forEach { code ->
                        DropdownMenuItem(
                            text = { Text(code) },
                            onClick = {
                                onCountryCodeChange(code)
                                onCountryCodeExpandedChange(false)
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = selectedNumber,
                onValueChange = onNumberChange,
                modifier = Modifier.weight(1f),
                label = { Text("Contact Number", fontSize = labelFontSize) },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(
                    fontFamily = urbanist
                )
            )
        }

        Spacer(modifier = Modifier.height(spacing * 1.5f))

        Text(
            text = "About",
            fontSize = if (screenWidth <= 360) 18.sp else 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = urbanist,
            modifier = Modifier.padding(bottom = spacing / 2)
        )

        OutlinedTextField(
            value = selectedAbout,
            onValueChange = onAboutChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 140.dp),
            placeholder = { Text("Tell us about yourself...", fontSize = labelFontSize) },
            textStyle = TextStyle(
                fontFamily = urbanist
            )
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DatePickerWithDialog(
    modifier: Modifier = Modifier,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
) {
    val dateState = rememberDatePickerState()
    val millisToLocalDate = dateState.selectedDateMillis?.let {
        DateUtils().convertMillisToLocalDate(it)
    }

    val configuration = LocalConfiguration.current

    val dateToString = millisToLocalDate?.let {
        DateUtils().dateToString(it)
    } ?: selectedDate

    var showDialog by remember { mutableStateOf(false) }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val spacing = if (screenWidth <= 360) 8.dp else 12.dp
    val labelFontSize = if (screenWidth <= 360) 12.sp else 14.sp
    Box(
        modifier = modifier
            .fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {

        OutlinedTextField(
            value = dateToString,
            onValueChange = {},
            readOnly = true,
            label = { Text("Date", fontSize = labelFontSize) },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    modifier = Modifier
                        .clickable(onClick = { showDialog = true })
                )
            },
            modifier = Modifier
                .clickable(onClick = { showDialog = true }),
            maxLines = 1,
            textStyle = TextStyle(
                fontSize = labelFontSize,
                fontFamily = urbanist
            )
        )
        if (showDialog) {
            DatePickerDialog(
                onDismissRequest = { showDialog = false },
                confirmButton = {
                    Button(
                        onClick = {
                            millisToLocalDate?.let {
                                val dateStr = DateUtils().dateToString(it)
                                onDateSelected(dateStr) // 🔥 Pass selected date to parent
                            }
                            showDialog = false
                        }) {
                        Text(text = "OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = { showDialog = false }
                    ) {
                        Text(text = "Cancel")
                    }
                }
            ) {
                DatePicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    state = dateState,
                    showModeToggle = true,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White
                    )
                )
            }
        }
    }
}