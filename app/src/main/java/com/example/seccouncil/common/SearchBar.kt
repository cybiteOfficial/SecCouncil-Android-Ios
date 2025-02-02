package com.example.seccouncil.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(showSystemUi = true)
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
    BoxWithConstraints {
        val screenWidth = maxWidth
        val screenHeight = maxHeight

        // Adjust sizes based on screen width and height
        val iconSize = (screenWidth / 20).coerceAtLeast(24.dp)  // Minimum 24.dp
        val height = (screenHeight / 15).coerceAtLeast(50.dp)   // Minimum 50.dp
        val elevation = 2.dp // Fixed low elevation for a subtle shadow

        Box(
            modifier = modifier
                .clip(RoundedCornerShape(cornerRadius))
                .background(backgroundColor)
                .shadow(
                    elevation = elevation,
                    shape = RoundedCornerShape(cornerRadius),
                    clip = false
                )
                .padding(4.dp) // Padding outside the shadow for better visual effect
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                modifier = Modifier
                    .height(height)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(cornerRadius))
                    .background(backgroundColor),
                placeholder = {
                    Text(
                        text = placeholderText,
                        fontSize = 12.sp,
                        color = placeholderTextColor
                    )
                },
                leadingIcon = {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = leadingIconImageVector,
                            contentDescription = "Leading Icon",
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
                    fontSize = 12.sp,
                    color = Color.Black
                )
            )
        }
    }
}