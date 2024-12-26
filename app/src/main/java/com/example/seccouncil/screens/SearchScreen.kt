package com.example.seccouncil.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R

@Preview(showBackground = true)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.weight(1f))
            SearchField(
                imeAction = ImeAction.Done
            )
        }
        Spacer(Modifier.height(25.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp)
            ,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SearchHistory()
            SearchHistory()
            SearchHistory()
            SearchHistory()
            SearchHistory()
            SearchHistory()
            SearchHistory()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SearchHistory(
    modifier:Modifier = Modifier
){
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search Icon",
            modifier = Modifier.size(27.dp)
        )
        Spacer(Modifier.width(15.dp))
        Text(
            text = "Cyber security course",
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.weight(1f)) // Pushes the last icon to the end
        Icon(
            painter = painterResource(R.drawable.outline_arrow_outward_24),
            contentDescription = "Search Icon",
            modifier = Modifier.size(30.dp)
                .graphicsLayer {
                    scaleX = -1f        // Flipping the outline_arrow_outward_24
                },
            tint = Color.Gray

        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchField(
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    value:String=""
){
    TextField(
        value = value,
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
            .padding(start = 20.dp)
            .clip(RoundedCornerShape(10.dp))
            .border((0.5).dp,Color.Gray, RoundedCornerShape(10.dp))
        ,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            if(value!=""){
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear",
                    modifier = Modifier.size(16.dp)
                )
            }

        }
    )
}