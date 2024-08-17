package com.grocery.flash.ui

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun otpScreen(otp: String, flashViewModel: FlashViewModel) {
    otpScreenTextBox(otp, flashViewModel)
}

@Composable
fun otpScreenTextBox(otp: String, flashViewModel: FlashViewModel) {
    val context = LocalContext.current
    val verificationId by flashViewModel.verificationId.collectAsState()

    BasicTextField(
        value = otp, onValueChange = {
            flashViewModel.setOtp(it)
        }, modifier = Modifier
            .fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 30.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(6) { index -> // six times we need to create column for Otp eg (_ _ _ _ _ _)
                    val number = when {
                        index >= otp.length -> ""
                        else -> otp[index].toString()
                    }
                    Column( // one column is for one digit
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(
                            text = number,
                            fontSize = 35.sp,
                            color = Color.White
                        ) // to display the otp on spaceholder like _ on above underscore digit
                        Box( // and box is wrapped with column
                            modifier = Modifier
                                .width(40.dp)
                                .height(2.dp)
                                .background(Color.LightGray)
                        ) {

                        }
                    }
                }
            }
            Button(
                onClick = {
                    if (otp.isEmpty()) {
                        Toast.makeText(context, "Enter the OTP", Toast.LENGTH_SHORT).show()
                    } else {
                        val credential = PhoneAuthProvider.getCredential(verificationId!!, otp)
                        Toast.makeText(
                            context,
                            "verification ${verificationId} credential ${credential}",
                            Toast.LENGTH_SHORT
                        ).show()
                        signInWithPhoneAuthCredential(credential, context,flashViewModel)
                    }
                }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black
                )
            ) {

                Text(text = "Verify", fontSize = 25.sp)
            }
        }
    }
}

private fun signInWithPhoneAuthCredential(
    credential: PhoneAuthCredential,
    context: Context,
    flashViewModel: FlashViewModel
) {
    firebaseAuth.signInWithCredential(credential)
        .addOnCompleteListener(context as Activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                // Log.d("Signing firebase success", "signInWithCredential:success")
                Toast.makeText(context, "Verification SuccessFul", Toast.LENGTH_SHORT).show()
                val user = task.result?.user
                if (user != null) {
                    flashViewModel.setUserCred(user)
                }
            } else {
                // Sign in failed, display a message and update the UI
//                Log.w("Signing firebase error", "signInWithCredential:failure", task.exception)
                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                    // The verification code entered was invalid
                    Toast.makeText(context, "Invalid OTP, please re-enter again", Toast.LENGTH_SHORT).show()
                }
                // Update UI
            }
        }
}
