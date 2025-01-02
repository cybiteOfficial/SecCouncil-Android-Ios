package com.example.seccouncil.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.BottomContent
import com.example.seccouncil.common.BottomText
import com.example.seccouncil.common.Dividerr
import com.example.seccouncil.ui.theme.urbanist


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUp(
    onLoginClick:()->Unit = {}
){
   Column(
    modifier = Modifier
        .fillMaxSize()
        .navigationBarsPadding()
        .statusBarsPadding()
        .verticalScroll(state=rememberScrollState())
   ){
       SignUpContent()
       BottomContent()
       Spacer(Modifier.weight(1f))
       BottomText(
           normaltext = "Already have an account?",
           clickabletext = "Login Now",
           onClick = onLoginClick
       )
       Spacer(Modifier.height(28.dp))
       }
   }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpContent(
    onLoginClick: () -> Unit = {}
){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Hello! Register to get\nstarted",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontFamily = urbanist,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier
                    .padding(start = 15.dp, top = 80.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            InputField(placeHolderText = "Username", imeAction = ImeAction.Next)
            Spacer(Modifier.height(12.dp))
            InputField(placeHolderText = "Email", imeAction = ImeAction.Next)
            Spacer(Modifier.height(12.dp))
            InputField(placeHolderText = "Password", imeAction = ImeAction.Next)
            Spacer(Modifier.height(12.dp))
            InputField(placeHolderText = "Confirm password", imeAction = ImeAction.Done)
            Spacer(Modifier.height(30.dp))
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
                ,
                colors = ButtonColors(
                    containerColor = Color.Black,
                    disabledContainerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = "Register",
                    fontFamily = FontFamily.SansSerif,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
            Spacer(Modifier.height(35.dp))
            Dividerr()
            Spacer(Modifier.height(16.dp))
        }

    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    placeHolderText:String,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    containerColor: Color = colorResource(R.color.edit_box_background)
){
    TextField(
        value = "",
        onValueChange = {},
        modifier = Modifier.fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
            .clip(RoundedCornerShape(10.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = containerColor,
            unfocusedContainerColor = containerColor,
            disabledContainerColor = containerColor,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = imeAction
        ),
        placeholder = { Text(
            text = placeHolderText,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            color = colorResource(R.color.place_holder),
            modifier = Modifier.padding(start = 15.dp)
        ) }
    )
}






