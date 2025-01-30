package com.example.seccouncil.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.seccouncil.R
import com.rejowan.ccpc.CCPTransformer
import com.rejowan.ccpc.CCPValidator
import com.rejowan.ccpc.Country
import com.rejowan.ccpc.CountryCodePicker

// i faced a problem whose solution needed a concept of state Hosting

@Preview(showBackground = true)
@Composable
fun CountryPicker(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    country:Country,
    onCountryCodeChange:(Country)->Unit
){

    val context = LocalContext.current

    val validatePhoneNumber = remember(context) {
        CCPValidator(context = context)
    }

    var isNumberValid: Boolean by rememberSaveable(country, phoneNumber) {
        mutableStateOf(
            validatePhoneNumber(
                number = phoneNumber, countryCode = country.countryCode
            ),
        )
    }
    TextField(
        value = phoneNumber,
        onValueChange = {
                newPhoneNumber ->
            onPhoneNumberChange(newPhoneNumber)
            isNumberValid = validatePhoneNumber(
                number = newPhoneNumber, countryCode = country.countryCode)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.edit_box_background),
            unfocusedContainerColor = colorResource(R.color.edit_box_background),
            disabledContainerColor = colorResource(R.color.edit_box_background),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth(0.99f)
            .padding(start = 25.dp, end = 15.dp)
            .clip(RoundedCornerShape(10.dp)),
        textStyle = MaterialTheme.typography.bodyMedium,
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        placeholder = {
            Text(
                text = "Enter Your Phone Number",
                fontFamily = FontFamily.SansSerif,
                fontWeight = FontWeight.Normal,
                color = colorResource(R.color.place_holder),
                fontSize = 12.sp
            )
        },
//        label = {
//            Text(
//                text = "Phone Number", style = MaterialTheme.typography.bodyMedium
//            )
//        },
        leadingIcon = {
            CountryCodePicker(
                selectedCountry = country,
                countryList = Country.getAllCountries(),
                onCountrySelected = {
                        newCountry ->
                    onCountryCodeChange(newCountry)
                    isNumberValid = validatePhoneNumber(
                        number = phoneNumber, countryCode = newCountry.countryCode
                    )
                },

                )

        },
        isError = !isNumberValid && phoneNumber.isNotEmpty(),
        visualTransformation = CCPTransformer(context, country.countryIso),

        )

}