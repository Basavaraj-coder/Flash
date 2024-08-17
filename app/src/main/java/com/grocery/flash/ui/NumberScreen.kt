package com.grocery.flash.ui

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun NumberScreen(
    flashViewModel: FlashViewModel,
    callbacks :PhoneAuthProvider.OnVerificationStateChangedCallbacks
) {
    val phoneNumber by flashViewModel.phon_number.collectAsState()
    val context = LocalContext.current
    Column (
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        Text(
            "LoGiN",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Enter your phone number to proceed",
            fontSize = 22.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Text(
            text = "You will receive OTP to this number",
            fontSize = 20.sp,
            color = Color(105, 103, 100),
        )
        TextField(
            value = phoneNumber,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
            ),
            onValueChange = {
                flashViewModel.setPhoneNumber(it)
            }, label = {
                Text(text = "Your number")
            },
            modifier = Modifier.fillMaxWidth().background(Color.LightGray),
            singleLine = true,
        )
        Button(onClick = {

            val options = PhoneAuthOptions.newBuilder(firebaseAuth )
                .setPhoneNumber("+91${phoneNumber}") // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                .setActivity(context as Activity) // Activity (for callback binding)
                .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Send OTP")
        }
    }
}
//  value = phoneNumber,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Number
//        ),
//        onValueChange = ""