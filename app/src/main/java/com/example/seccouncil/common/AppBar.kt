package com.example.seccouncil.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    modifier: Modifier = Modifier,
    title:String = "Hello",
    fontSize:TextUnit = 20.sp,
    onClick: () -> Unit = {},
    content:@Composable () -> Unit = {},
    fontWeight: FontWeight = FontWeight.ExtraBold,
    showTrailingIcon:Boolean = true,
    iconSize: Dp = 32.dp
){
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
                        modifier = Modifier.fillMaxWidth()
                            .padding(end = 15.dp)
                        ,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.weight(1f)) // Push content to the center
                        Text(
                            text = title,
                            textAlign = TextAlign.Center,
                            fontSize = fontSize,
                            fontFamily = FontFamily.Serif,
                            fontWeight = fontWeight
                        )
                        Spacer(modifier = Modifier.weight(1f)) // Push the icon to the end
                        if(showTrailingIcon)
                        Icon(
                            imageVector = Icons.Default.Add,
                            modifier = Modifier
                                .size(width = 21.dp, height = 28.dp),
                            contentDescription = title
                        )

                    }
                },
                navigationIcon = {
                    IconButton(onClick = onClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(iconSize)
                        )
                    }
                }
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerpadding)
                .padding(horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
               content()
        }
    }
}
