package com.example.seccouncil.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.ui.theme.urbanist

@Preview(showSystemUi = true)
//@Composable
//fun ResponsiveSearchBar(
//    value: String = "",
//    onValueChange: (String) -> Unit = {},
//    modifier: Modifier = Modifier,
//    placeholderText: String = "Search courses",
//    placeholderTextColor: Color = Color.Gray,
//    strokeColor: Color = Color.Gray,
//    backgroundColor: Color = Color.White,
//    cornerRadius: Dp = 12.dp,
//    strokeWidth: Dp = 1.dp,
//    leadingIconImageVector: ImageVector = Icons.Outlined.Search
//) {
//        BoxWithConstraints(
//            modifier =
//                Modifier
//                    .shadow(
//                        elevation = 5.dp,
//                        shape = RoundedCornerShape(12.dp),
//                        ambientColor = Color.LightGray,
//                        spotColor = Color.DarkGray
//                    )
//                    .border(
//                        width = 1.dp,
//                        color = Color.White, // ✅ Adds a light border for better distinction
//                        shape = RoundedCornerShape(12.dp),
//                    )
//                    .clip(RoundedCornerShape(12.dp)) // Clip corners to match shadow shape
//                    .background(
//                        color = Color.White,
//                        shape = RoundedCornerShape(12.dp)) // Background color of the Box
//        ) {
//            val screenWidth = maxWidth
//            val screenHeight = maxHeight
//
//            // Adjust sizes based on screen width and height
//            val iconSize = (screenWidth / 20).coerceAtLeast(24.dp)  // Minimum 24.dp
//            val height = (screenHeight / 15).coerceAtLeast(50.dp)   // Minimum 50.dp
//            Box(
//                modifier = modifier
//                    .clip(RoundedCornerShape(cornerRadius))
//                    .background(Color.White)
//                    .shadow(
//                        elevation = 0.dp,
//                        shape = RoundedCornerShape(cornerRadius),
//                        clip = true
//                    )
//                    .padding(4.dp) // Padding outside the shadow for better visual effect
//            ) {
//                OutlinedTextField(
//                    value = value,
//                    onValueChange = onValueChange,
//                    singleLine = true,
//                    modifier = Modifier
//                        .height(height)
//                        .fillMaxWidth()
//                        .clip(RoundedCornerShape(cornerRadius))
//                        .background(Color.White),
//                    placeholder = {
//                        Text(
//                            text = placeholderText,
//                            fontSize = 12.sp,
//                            color = placeholderTextColor
//                        )
//                    },
//                    leadingIcon = {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxHeight()
//                                .wrapContentSize(Alignment.Center)
//                        ) {
//                            Icon(
//                                imageVector = leadingIconImageVector,
//                                contentDescription = "Leading Icon",
//                                modifier = Modifier.size(iconSize)
//                            )
//                        }
//                    },
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        disabledIndicatorColor = Color.Transparent
//                    ),
//                    textStyle = TextStyle.Default.copy(
//                        fontSize = 12.sp,
//                        color = Color.Black
//                    )
//                )
//            }
//        }
//    }

@Composable
fun ResponsiveSearchBar(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    placeholderText: String = "Search courses",
    placeholderTextColor: Color = Color.Gray,
    strokeColor: Color = Color.Gray,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 12.dp,
    strokeWidth: Dp = 1.dp,
    leadingIconImageVector: ImageVector = Icons.Outlined.Search
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // 📱 Determine screen-aware icon size (min 24.dp for visibility)
    val iconSize = (screenWidth / 20).coerceAtLeast(24.dp)

    // ✅ Adjust height based on screen width safely
    // If screenWidth is 360.dp → height = 360/8 = 45.dp (good enough)
    // We use coerceIn to ensure height is never too small or too big
    val textFieldHeight = (screenWidth / 8).coerceIn(52.dp, 54.dp)

    Box(
        modifier = modifier
            .shadow(
                elevation = 5.dp,
                shape = RoundedCornerShape(cornerRadius),
                ambientColor = Color.LightGray,
                spotColor = Color.DarkGray
            )
            .border(
                width = strokeWidth,
                color = strokeColor,
                shape = RoundedCornerShape(cornerRadius)
            )
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .padding(4.dp) // Outer padding for spacing
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            modifier = Modifier
                .height(textFieldHeight) // 👈 Responsive height
                .fillMaxWidth()
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor),
            placeholder = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentHeight(Alignment.CenterVertically) // 👈 Ensures proper vertical alignment
                ) {
                    Text(
                        text = placeholderText,
                        fontSize = 14.sp, // Slightly increased for clarity
                        color = placeholderTextColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            },
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .wrapContentSize(Alignment.Center)
                ) {
                    Icon(
                        imageVector = leadingIconImageVector,
                        contentDescription = "Search Icon",
                        modifier = Modifier.size(iconSize)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            textStyle = TextStyle.Default.copy(
                fontSize = 18.sp,
                color = Color.Black,
                fontFamily = urbanist
            )
        )
    }
}