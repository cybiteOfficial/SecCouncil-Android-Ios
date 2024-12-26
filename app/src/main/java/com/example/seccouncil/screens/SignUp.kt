package com.example.seccouncil.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SignUp(){
   Column(
    modifier = Modifier
        .safeDrawingPadding()
        .navigationBarsPadding()
        .verticalScroll(state=rememberScrollState())
   ){
       Card(
           modifier = Modifier.fillMaxWidth()
               .clip(RoundedCornerShape(bottomStart = 0.dp, bottomEnd = 0.dp))
       ) {
           Image(
               painter = painterResource(R.drawable.seec_1),
               contentDescription = null,
               modifier = Modifier.background(color = colorResource(R.color.image_background))
                   .fillMaxWidth(),
               contentScale = ContentScale.Crop
           )
       }
            // placing bottom card into top of top card
       Card(
           modifier = Modifier
               .fillMaxSize()
               .offset(y = (-20).dp) // Adjust the vertical position to overlap the first card
               .clip(RoundedCornerShape(20.dp)),
           colors = CardDefaults.cardColors(
               containerColor = colorResource(R.color.white),
               contentColor = colorResource(R.color.black),
               disabledContentColor = colorResource(R.color.black),
               disabledContainerColor = colorResource(R.color.white)
       )
       ){
            SignUpContent()
       }
   }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpContent(){
    Text(
        text = "Sign up",
        fontStyle = FontStyle.Normal,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.Serif,
        fontSize = 25.sp,
        modifier = Modifier.padding(start = 15.dp, top = 30.dp)
    )
    Spacer(modifier = Modifier.height(45.dp))
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        InputField(placeHolderText = "Full Name", imeAction = ImeAction.Next)
        Spacer(Modifier.height(20.dp))
        InputField(placeHolderText = "Enter Email/Phone No.", imeAction = ImeAction.Next)
        Spacer(Modifier.height(20.dp))
        InputField(placeHolderText = "Password", imeAction = ImeAction.Done)
        Spacer(Modifier.height(35.dp))
        Button(
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .height(50.dp),
            shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
            ,
            colors = ButtonColors(
                containerColor = colorResource(R.color.button_color),
                disabledContainerColor = colorResource(R.color.button_color),
                contentColor = Color.Black,
                disabledContentColor = Color.Black
            )

        ) {
            Text(
                text = "Sign up",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Row(
            modifier = Modifier
                .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Checkbox(
                checked = false,
                onCheckedChange = {},
                colors = CheckboxColors(
                    checkedBorderColor = Color.Gray, // Border color when checked
                    uncheckedBorderColor = Color.Gray, // Border color when unchecked,
                    checkedCheckmarkColor = Color.Black,
                    uncheckedCheckmarkColor = Color.Black,
                    uncheckedBoxColor = Color.Transparent, // No background when unchecked
                    checkedBoxColor = Color.Transparent, // No background when checked
                    disabledCheckedBoxColor = Color.Transparent, // Transparent background when disabled and checked
                    disabledUncheckedBoxColor = Color.Transparent, // Transparent background when disabled and unchecked
                    disabledBorderColor = Color.Gray, // Gray border for disabled checkbox
                    disabledUncheckedBorderColor = Color.Gray, // Gray border for disabled unchecked checkbox,
                    disabledIndeterminateBorderColor = Color.Transparent,
                    disabledIndeterminateBoxColor = Color.Transparent
                ),
                modifier = Modifier.padding(start = 3.dp)
            )
            Text(
                text = "I Agree with terms of service and Privacy Policy",
                fontFamily = FontFamily.Serif
            )
        }
       Dividerr()
        Spacer(Modifier.height(25.dp))
        BottomContent(
            normaltext = "Already have an account?",
            clickabletext = "Log in"
        )
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
            fontFamily = FontFamily.Monospace,
            color = colorResource(R.color.place_holder),
            modifier = Modifier.padding(start = 15.dp)
        ) }
    )
}

@Composable
fun Dividerr(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 15.dp), // Add vertical padding for spacing
        verticalAlignment = Alignment.CenterVertically // Vertically center content
    ) {
        HorizontalDivider(modifier = Modifier
            .weight(1f)
            .padding(start = 11.dp)
            .fillMaxWidth()
            , color = Color.Black
        ) // Line on the left
        Text(
            text="or",
            modifier = Modifier
                .align(Alignment.CenterVertically), // Text in the middle
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            fontStyle = FontStyle.Normal,
            color = Color.Black,
            fontFamily = FontFamily.Serif
        )
        HorizontalDivider(modifier = Modifier.weight(1f), color = Color.Black) // Line on the right
    }
}

@Composable
fun BottomContent(
    modifier: Modifier = Modifier,
    normaltext:String,
    clickabletext:String
) {
    Column() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                15.dp,
                alignment = Alignment.CenterHorizontally
            )
        ) {
            Image(
                painter = painterResource(R.drawable.google_login),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
            )
            Image(
                painter = painterResource(R.drawable.facebook_login),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                text = normaltext,
                fontFamily = FontFamily.Serif
            )
            ClickableText(
                text = AnnotatedString(
                    text = clickabletext
                ),
                onClick = {},
                style = TextStyle(
                  //  textDecoration = TextDecoration.Underline,
                    color = Color.Blue,
                    fontFamily = FontFamily.SansSerif
                ),
                modifier = Modifier
                    .padding(start = 5.dp),
            )
        }
    }
}


