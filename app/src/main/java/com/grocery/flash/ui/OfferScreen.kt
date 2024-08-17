package com.grocery.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
//import com.example.flash.R
import com.grocery.flash.R

@Composable

fun OfferScreen() {
//splash screen
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(Color.Transparent)
    ){
        Image(
            painter = painterResource(id = R.drawable.offerscreen),
            contentDescription = "offerscreen",
            Modifier.padding(20.dp)
        )
    }
}