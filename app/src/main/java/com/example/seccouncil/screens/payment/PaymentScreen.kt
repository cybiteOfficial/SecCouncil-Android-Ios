package com.example.seccouncil.screens.payment

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R


@Preview(showBackground =true, showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    modifier: Modifier = Modifier
){
    val options = listOf("Paypal", "Paytm", "Google Pay","Other methods")
    val img = listOf(R.drawable.paypal,R.drawable.paytm,R.drawable.google_pay,R.drawable.card_payment)
    var selectedOption by remember { mutableStateOf(options[0]) }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
        ,
        topBar = {
            TopAppBar(
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
                            text = "Payment",
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.weight(1f)) // Push the icon to the end
                        Icon(
                            imageVector = Icons.Default.Add,
                            modifier = Modifier
                                .size(width = 21.dp, height = 28.dp),
                            contentDescription = "Add More Payment Option"
                        )

                    }
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { innerpadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerpadding)
                .padding(horizontal = 15.dp)
            ,
        ) {
            Spacer(Modifier.height(20.dp))
            options.forEachIndexed{
                index,option->
                    PaymentItem(
                        paymentName = option,
                        selectedOption = selectedOption,
                        onOptionSelected = {selectedOption=it}, // Update callback
                        option = option,
                        image = img[index]
                    )
                Spacer(Modifier.height(15.dp))
            }
        }
    }
}


@Composable
private fun PaymentItem(
    modifier: Modifier = Modifier,
    @DrawableRes image:Int,
    paymentName:String = "Paypal",
    option:String,
    selectedOption:String,
    onOptionSelected:(String)->Unit // Callback to update selected option
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = "Paypal",
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(10.dp))
            Text(
                text = paymentName,
                fontFamily = FontFamily.Monospace,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black
            )
            Spacer(Modifier.weight(1f))
            RadioButton(
                selected = selectedOption == option,
                onClick = { onOptionSelected(option) },
                modifier=Modifier
                    .size(22.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        HorizontalDivider(
            color = Color.LightGray
        )
    }

}

