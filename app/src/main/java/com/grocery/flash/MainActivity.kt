package com.grocery.flash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
//import com.example.flash.ui.flashApp
import com.grocery.flash.ui.flashApp
//import com.example.flash.ui.theme.FlashTheme
import com.grocery.flash.ui.theme.FlashTheme

class MainActivity : ComponentActivity() {
    //val flashViewmodel : FlashViewModel by viewModels()
    //Firstly you are initializing your viewmodel by viewModel() but it has to be viewModels().
    //
    //And as I told you before don't use '=' insted use 'by' keyword to initialize.
    //
    //Even if doesn't work then check that you have implemented the dependency for viewModels properly.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlashTheme {
                // A surface container using the 'background' color from the theme
                Surface( //surface here is like layout in xml u can relate
                    modifier = Modifier.fillMaxSize(), // modifier give a size to surface, simple
                    color = MaterialTheme.colorScheme.background,
                  //  contentColor = MaterialTheme.colorScheme.contentColorFor(Color.White)
                ) {
                    //Greeting("Android")

                    flashApp()
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
//    val mockFlashViewModel = FlashViewModel().apply {
//        // Set up mock data here if necessary
////        val flashUiState by FlashViewModel().uiState.collectAsState()
////        flashUiState.clickStatus = ""
//    }

    FlashTheme {
        flashApp()
    }
}