package com.example.seccouncil.common

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.example.seccouncil.screens.homescreen.AboutSection
import com.example.seccouncil.screens.homescreen.LessonsSection
import com.example.seccouncil.screens.homescreen.ReviewsSection
import kotlin.math.roundToInt

@Composable
fun TabScreen(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = 15.dp,
    tabTitles:List<String> = listOf("About","Lessons","Reviews"),
    density: Density = LocalDensity.current,
    configuration: Configuration = LocalConfiguration.current,

    ) {
    val tabTitles = tabTitles
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val screenWidthPixels = (configuration.screenWidthDp) * density.density
    val tabWidthFraction = 1f / tabTitles.size
    val tabWidthDp =  with(density) {
        (tabWidthFraction * (screenWidthPixels - (horizontalPadding.toPx() * 2))).toDp()
    }

    Column(
        modifier = modifier
    )
    {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = Modifier
                .fillMaxWidth(),
            indicator = {}
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y=(10).dp)
                        .weight(1f),
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = {
                        Text(
                          text =  title,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            color = if(selectedTabIndex == index) colorResource(R.color.markallasread) else Color.Black
                        )
                    }
                )
            }
        }
        MyTabIndicator(
            selectedTabIndex = selectedTabIndex,
            tabWidth = tabWidthDp
        )
        Spacer(Modifier.height(20.dp))
        when(selectedTabIndex){
            1-> LessonsSection()
            2 -> ReviewsSection()
            else -> AboutSection()
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MyScreenPreview() {
    TabScreen(tabTitles = listOf("About","Lessons","Reviews"))
}

@Preview(showBackground = true)
@Composable
fun MyTabIndicator(
    selectedTabIndex: Int = 0,
    tabWidth:Dp = 30.dp
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(3.dp)
            .background(
                Color.LightGray
            )
    ) {
        Box(
            Modifier
                .offset {
                    IntOffset(
                        x = (selectedTabIndex * tabWidth.toPx()).roundToInt(),
                        y = 0
                    )
                }
                .width(tabWidth)
                .height(3.dp)
                .background(
                    color = colorResource(R.color.markallasread),
                    shape = RoundedCornerShape(4.dp)
                )
        )
    }
}