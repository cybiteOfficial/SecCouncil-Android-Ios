package com.example.seccouncil.common

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.ui.theme.urbanist


@Preview(showBackground = true)
@Composable
fun RatingContent(
    level:String = "Easy",
    courseRating:String = "4.7",
    totalGivenRating:String = "25"
){
    Row(
        modifier = Modifier.wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        TextComm(
            text = level,
            fontSize = 12.sp
        )
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            modifier = Modifier.size(14.dp),
            tint = colorResource(R.color.starcolor)
        )
        val text = buildAnnotatedString {
            append("$courseRating")
            withStyle(style = SpanStyle(Color.Gray)){
                append(" ($totalGivenRating)")
            }
        }
        Text(
            text = text,
            fontSize = 12.sp,
            fontFamily = urbanist
        )

    }
}

@Preview(showBackground = true)
@Composable
fun EnrolledContent(
    subject: String = "14",
    showEnroll:Boolean = true,
    tint:Color =  Color.Unspecified,
    subjectAuthor: String = "None",
    subjectTime: String = "12h 30min",
    rating: String = "5.0"
){
    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        EnrollSubContent(
            tint = tint,
            icon = R.drawable.baseline_person_24,
            subject = subjectAuthor
        )
        Spacer(Modifier.width(20.dp))
        EnrollSubContent(
            icon = R.drawable.star,
            subject = rating,
            tint = tint
        )
        if(showEnroll){
            Spacer(Modifier.width(20.dp))
            EnrollSubContent(
                icon = R.drawable.people,
                subject = "Enrolled",
                tint = tint
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnrollSubContent(
   @DrawableRes icon:Int =  R.drawable.clock,
   subject:String = "12h 52m",
   contentDescription:String = "",
   tint:Color =  Color.Unspecified
){
    Row(
        modifier = Modifier
            .wrapContentWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Icon(
            painter = painterResource(icon),
            contentDescription = contentDescription,
            modifier = Modifier.size(14.dp),
            tint = tint
        )
        Spacer(Modifier.width(4.dp))
        TextComm(
            text = subject,
            fontSize = 12.sp,
        )
    }

}


@Composable
fun LinearProgressBarWithCircle(
    finished: Int = 11,
    total: Int = 20
) {
    val totalWidth = 311.dp // Total width of the indicator
    val filledWidth = (totalWidth.value * (finished.toFloat() / total)).dp

    Box(
        modifier = Modifier.width(totalWidth),
        contentAlignment = Alignment.CenterStart
    ) {
        // Background progress bar
        Box(
            modifier = Modifier
                .height(5.dp)
                .width(totalWidth)
                .background(Color.Gray, shape = RoundedCornerShape(2.5.dp))
        ) {
            // Foreground progress bar
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .width(filledWidth)
                    .background(Color.White, shape = RoundedCornerShape(2.5.dp))
            )
        }

        // Circle at the end of the foreground progress bar
        Box(
            modifier = Modifier
                .size(15.dp)
                .offset(x = filledWidth)
                .background(Color.White, shape = CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LinearProgressBarWithCirclePreview() {
    LinearProgressBarWithCircle(finished = 17, total = 20)
}


@Composable
fun ViewAllTitle(
    title:String = "Popular Courses"
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontFamily = urbanist,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "View all",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ButtonComm(
    text:String = "",
    onClick:()->Unit = {},
    modifier: Modifier = Modifier
){
    Button(
        onClick = onClick,
        modifier =modifier.wrapContentWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(8.dp)
    ){
        TextComm(
            text = text,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 20.88.sp,
            modifier = Modifier,
        )
    }
}