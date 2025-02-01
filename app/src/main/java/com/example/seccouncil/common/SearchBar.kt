package com.example.seccouncil.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//@Composable
//fun SearchBar(
//    modifier: Modifier = Modifier
//){
//    Box(
//        modifier = Modifier
//            .wrapContentSize()
//            .clip(RoundedCornerShape(12.dp)) // Clip the content
//            .drawBehind { // Custom stroke effect
//                val strokeWidth = 3.dp.toPx() // Stroke width
//                val cornerRadius = 12.dp.toPx() // Corner radius
//                drawRoundRect(
//                    color = Color.Gray, // Stroke color
//                    size = size.copy(
//                        width = size.width - strokeWidth,
//                        height = size.height - strokeWidth
//                    ),
//                    topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
//                    cornerRadius = CornerRadius(cornerRadius, cornerRadius),
//                    style = Stroke(width = strokeWidth)
//                )
//            }
//            .background(Color.White) // Background color inside stroke
//    ) {
//        OutlinedTextField(
//            value = "",
//            onValueChange = { "" },
//            modifier = Modifier
//                .height(50.dp)
//                .fillMaxWidth()
//                .background(Color.Transparent)
//                .clip(RoundedCornerShape(10.dp)), // Inner content clipping
//            placeholder = {
//                Text(
//                    text = "Search courses",
//                    textAlign = TextAlign.Center,
//                    fontSize = 16.sp,
//                    color = Color.Gray
//                )
//            },
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Outlined.Search,
//                    contentDescription = "Search",
//                    modifier = Modifier.size(24.dp)
//                )
//            },
//            colors = TextFieldDefaults.colors(
//                focusedContainerColor = Color.Transparent,
//                unfocusedContainerColor = Color.Transparent,
//                disabledContainerColor = Color.Transparent,
//                focusedIndicatorColor = Color.Transparent,
//                unfocusedIndicatorColor = Color.Transparent,
//                disabledIndicatorColor = Color.Transparent
//            )
//        )
//    }
//
//}

//
@Preview(showSystemUi = true)
@Composable
fun SearchBar(
    value: String = "",
    onValueChange: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    placeholderText: String = "Search courses",
    placeholderTextColor: Color = Color.Gray,
    strokeColor: Color = Color.Gray,
    backgroundColor: Color = Color.White,
    cornerRadius: Dp = 12.dp,
    strokeWidth: Dp = 3.dp,
    leadingIconImageVector: ImageVector = Icons.Outlined.Search
) {
        // The primary Box that holds the OutlinedTextField
        Box(
            modifier = modifier
                // Rounded corners for the container
                .clip(RoundedCornerShape(cornerRadius))
                // Use shadow with clip = false so the entire shadow is shown uniformly
                .shadow(
                    elevation = 3.dp,  // Increase elevation for a more pronounced shadow
                    shape = RoundedCornerShape(cornerRadius),
                    clip = true,
                    ambientColor = Color.LightGray,   // You can change or remove these custom colors
                    spotColor = Color.Gray
                )
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true, // Keeps the text on a single line
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    // No additional background color needed, let it be transparent
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(cornerRadius)), // Match the corner radius
                placeholder = {
                    // Vertically center the placeholder text
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Text(
                            text = placeholderText,
                            textAlign = TextAlign.Center,
                            fontSize = 12.sp,
                            color = placeholderTextColor
                        )
                    }
                },
                leadingIcon = {
                    // Vertically center the icon
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = leadingIconImageVector,
                            contentDescription = "Leading Icon",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                // Remove the default indicator and background colors
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                // Center the typed text horizontally
                textStyle = LocalTextStyle.current.copy(
                    textAlign = TextAlign.Center
                )
            )
        }
    }