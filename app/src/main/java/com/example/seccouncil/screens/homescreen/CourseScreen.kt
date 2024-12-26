package com.example.seccouncil.screens.homescreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.common.TabScreen
import kotlin.text.append

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CourseScreen(
    modifier: Modifier = Modifier
){
    var isFavorited by remember {
        mutableStateOf(false)
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .safeDrawingPadding()
            .navigationBarsPadding()
            .padding(horizontal = 15.dp)
            .verticalScroll(rememberScrollState())

    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (isFavorited)Icons.Filled.Favorite
                else Icons.Filled.FavoriteBorder,
                contentDescription =  if (isFavorited) "Remove from Favorites" else "Add to Favorites",
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        isFavorited = !isFavorited
                    },
                tint = if (isFavorited) Color.Red else Color.Gray
            )
        }
        Spacer(Modifier.height(10.dp))
        Image(
            painter = painterResource(R.drawable.c0),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .height(200.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(35.dp))
        CourseName()
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            CourseItem(modifier.weight(1f))
            CourseItem(modifier.weight(1f), text = "5 hours", image = R.drawable.clock)
            CourseItem(modifier.weight(1f), text = "Certificate",image = R.drawable.congrate)
        }
        Spacer(modifier=Modifier.height(5.dp))
        TabScreen()
        Spacer(modifier.height(3.dp))
        CourseEnroll()

    }
}

@Composable
private fun CourseName(
    modifier: Modifier = Modifier,
    courseName:String = "Cyber Security Begineer",
    rating:String = "4.7",
    subject:String = "Coding",
    no_of_reviews:Int = 761
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
           text = courseName,
            fontFamily = FontFamily.Monospace,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(15.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                painter = painterResource(R.drawable.star),
                contentDescription = null,
                tint = colorResource(R.color.starcolor)
            )
            Spacer(Modifier.width(5.dp))
            Text(
                text = "${rating}/5 (${no_of_reviews} reviews)",
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Light
            )
            Spacer(Modifier.width(10.dp))
            VerticalDivider(
                modifier=Modifier
                    .height(15.dp),
                    thickness = 1.dp,
                color = Color.Black
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = subject,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Light,
                color = colorResource(R.color.markallasread)
            )


        }
    }
}

@Composable
private fun CourseItem(
    modifier: Modifier = Modifier,
    @DrawableRes image:Int = R.drawable.people,
    text :String = "1,102 students"
){
    Box(
        modifier = modifier
            .width(104.dp)
            .height(76.dp)
            .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
    ){
        Column(
            modifier = modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(image) ,
                contentDescription = null,
                modifier = Modifier
                    .size(32.dp)
            )
            Text(
                text = text,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
fun AboutSection(
    modifier: Modifier = Modifier,
    title:String = "About Course",
    description:String =
        "Lorem Ipsum is simply dummy text of the printing and " +
                "typesetting industry. Lorem Ipsum has been the " +
                "industry's standard dummy text ever since the 1500s," +
                "when an unknown printer took a galley of type and" +
                "scrambled it to make a type specimen book. It has " +
                "survived not only five centuries, but also the leap into" +
                "electronic typesetting, remaining essentially unchanged." +
                "It was popularised in the 1960s with the release of" +
                "Letraset sheets containing Lorem Ipsum passages, and" +
                "more recently with desktop publishing software like" +
                "Aldus PageMaker including versions of Lorem Ipsum."
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(Modifier.height(15.dp))
        Text(
            text = description,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.DarkGray
        )
    }
}


@Preview(showBackground = true)
@Composable
fun LessonsSection(
    modifier: Modifier = Modifier
){
    Column(
        modifier =Modifier
            .fillMaxWidth()
            .height(230.dp)
        ,
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        LessonSectionItem()
        LessonSectionItem()
    }
}
@Composable
fun LessonSectionItem(
    @DrawableRes img:Int = R.drawable.frame_32,
    title: String = "Introduction | Part-01",
   @DrawableRes icon:Int = R.drawable.circled_play
){
    Column(
        modifier =Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp) // Padding inside the Row
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(img),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = title,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(Modifier.height(15.dp))
        HorizontalDivider(
            thickness = 2.dp,
            color = Color.LightGray
        )

    }
}

@Preview(showBackground = true)
@Composable
fun RatingIndicatorBox(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .width(258.dp)
            .height(132.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        RatingIndicator(ratingValue = 5)
        RatingIndicator(ratingValue = 4)
        RatingIndicator(ratingValue = 3)
        RatingIndicator(ratingValue = 2)
        RatingIndicator(ratingValue = 1)
    }
}

@Preview(showBackground = true)
@Composable
fun RatingIndicator(
    ratingValue: Int = 5,
    maxRatingValue: Int = 5
) {
    val density = LocalDensity.current
    val totalWidth = with(density) { 258.dp } // Total width of the indicator
    val filledWidth = with(density) { (totalWidth.value * (ratingValue.toFloat() / maxRatingValue)).dp }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        Text(
            text = "$ratingValue",
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.DarkGray
        )
        Box(
            Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(
                    Color.LightGray
                )
        ) {
            Box(
                Modifier
                    .width(filledWidth)
                    .height(10.dp)
                    .background(
                        color = colorResource(R.color.markallasread),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsSection(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .size(
                width = 394.dp,
                height = 230.dp
            )
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "Summary",
            fontFamily = FontFamily.Monospace,
            fontSize = 18.sp,
            color = Color.DarkGray,
            modifier = modifier.align(Alignment.CenterHorizontally)
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(22.dp)
        ){
            RatingIndicatorBox()
            Column(
                modifier = Modifier
                    .size(width = 82.dp, height = 132.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ){
                    Text(
                        text = "4.5",
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = colorResource(R.color.markallasread),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier.height(8.dp))
                Text(
                    text = "273 Reviews",
                    fontFamily = FontFamily.Serif,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier.height(30.dp))
                Text(
                    text = "88%",
                    fontFamily = FontFamily.Serif,
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier.height(8.dp))
                Text(
                    text = "Recommended",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

            }
        }

    }
}

@Preview(showBackground = true)
@Composable
private fun CourseEnroll(
    modifier: Modifier = Modifier
){
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(10.dp) // To Clip the Corner of the Button
        ,
        colors = ButtonColors(
            containerColor = Color.LightGray,
            disabledContainerColor = Color.LightGray,
            contentColor = Color.Black,
            disabledContentColor = Color.Black
        )

    ) {
        val fullText = buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.DarkGray)){
                append("Enroll course -")
            }
            withStyle(style = SpanStyle(color = colorResource(R.color.markallasread))) {
                append(" ₹750.00")
            }
        }
        Text(
            text = fullText,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )
    }
}