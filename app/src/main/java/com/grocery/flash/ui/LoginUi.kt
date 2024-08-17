package com.grocery.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
//import com.example.flash.R
import com.grocery.flash.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

@Composable
fun LoginUi(flashViewModel: FlashViewModel) {
    val otp by flashViewModel.otp.collectAsState()
    val verificationId by flashViewModel.verificationId.collectAsState()

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
//            Log.d(TAG, "onVerificationCompleted:$credential")
//            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
//            Log.w(TAG, "onVerificationFailed", e)
//
//            if (e is FirebaseAuthInvalidCredentialsException) {
//                // Invalid request
//            } else if (e is FirebaseTooManyRequestsException) {
//                // The SMS quota for the project has been exceeded
//            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
//                // reCAPTCHA verification attempted with null Activity
//            }

            // Show a message and update the UI
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
//            // The SMS verification code has been sent to the provided phone number, we
//            // now need to ask the user to enter the code and then construct a credential
//            // by combining the code with a verification ID.
//            Log.d(TAG, "onCodeSent:$verificationId")
//
//            // Save verification ID and resending token so we can use them later
//            storedVerificationId = verificationId
//            resendToken = token
            flashViewModel.setVerificationId(verificationId)
            //flashViewModel.setOpt(otp)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 5.dp, horizontal = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.deliveryboy),
            contentDescription = "app icon",
            modifier = Modifier.size(100.dp)
        )
        if(verificationId.isEmpty()){
            NumberScreen(flashViewModel = flashViewModel,callbacks)
        }else{
            otpScreen(otp =otp , flashViewModel = flashViewModel)
        }
        
//        Text(text = "")
//        TextField(value = "", onValueChange =)
    }
}