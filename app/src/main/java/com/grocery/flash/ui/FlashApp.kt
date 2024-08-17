package com.grocery.flash.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.grocery.flash.Data.InternetApiItems
import com.grocery.flash.R
import com.grocery.flash.ui.theme.Pink0

enum class FlashAppScreen(val title: String) {
    Start("Welcome to FlashCart"),
    Items("Choose items"),
    Cart("Shopping Cart")

    //this above start,item,cart are references to destinations of navigation
    //this enum class is required to declare destinations in NavGraph,
    //-- once destinations are defined we can add routes between destinations using NavHost
}

var canNavigateBack = false // to check if there is screen available or not in backstack

//this is back icon, if screen is available than show back icon btn on appbar, initial value
// is false
val firebaseAuth = FirebaseAuth.getInstance()//firebase auth object

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun flashApp(
    flashViewmodel: FlashViewModel = viewModel(), // first this loads before app
    navController: NavHostController = rememberNavController()
) {
    val user by flashViewmodel.user.collectAsStateWithLifecycle() // this is lifecycle aware, collectAsState() is not lyfecycle aware
    firebaseAuth.currentUser?.let { flashViewmodel.setUserCred(it) } // if we remove this line, it ask
    // again ask user for Otp, here in above line we are using in build data persistence of FireBase

    //here we are attaching viewModel to Screens
    //this approach ensures that whenever there is change in UiState value--
    //--recomposition takes place for the composable using the flashUiState value
    //recomposition here is updating Ui based on changes in the states that depends on Ui
    //val flashViewmodel : FlashViewModel by viewModels()
    val isVisible by flashViewmodel.isVisible.collectAsState()
    // above code line is to achieve splash screen(offerscreen)
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FlashAppScreen.valueOf(
        backStackEntry?.destination?.route
            ?: FlashAppScreen.Start.name // current screen visible info
    )
    canNavigateBack = navController.previousBackStackEntry != null //it return true if expression is
    //-- not null, above expression is the check whether screen are available in stack or not?
    // if available than return true else false
    val cartItemsCount by flashViewmodel.cartItem.collectAsState()

    if (isVisible) {
        OfferScreen()
    } else if (user == null) {
        LoginUi(flashViewModel = flashViewmodel)
    } else {
        Scaffold( // why we need scafold, it gives us top,bottom and FloatingActnBtn
            // easy to use/implement them so use Scaffold
            topBar = {
                TopAppBar(colors = TopAppBarDefaults.smallTopAppBarColors(Pink0),
                    modifier = Modifier.fillMaxWidth(),
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = currentScreen.title,
                                fontSize = 18.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_logout_24),
                                contentDescription = "logoutICon",
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(22.dp)
                                    .clickable {
                                        firebaseAuth.signOut()
                                        flashViewmodel.clearUserCred()
                                    },
                            )
//                            Text(
//                                text = "LogOut",
//                                fontSize = 16.sp,
//                                modifier = Modifier.padding(end = 8.dp)
//                            )
                        }
//                        Row (){
//
//                        }
                    },
                    navigationIcon = {
                        if (canNavigateBack) {
                            IconButton(onClick = {
                                navController.navigateUp()
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.ArrowBack,
                                    contentDescription = "back button"
                                )
                            }
                        }
                    }
                )
            },
            bottomBar = {
                flashBottomAppBar(
                    navController = navController,
                    currentScreen = currentScreen,
                    cartItemsCount = cartItemsCount // list of items
                )
            }
        ) { it ->
            NavHost(
                navController = navController,
                startDestination = FlashAppScreen.Start.name,
                modifier = Modifier.padding(it)
            ) {
                composable(route = FlashAppScreen.Start.name) {
                    firstScreen(
                        flashViewModel1 = flashViewmodel,
                        onCategoryClicked = {
                            //whenever clicked on card the execution start from here beginning part of App
                            //the value passed to oncategoryclicked when the user clicks the card in
                            // startscreen will also be accessible in onCategoryClicked parameter lambda
                            // in the flashApp
                            flashViewmodel.updateSelectedCategory(it)
                            navController.navigate(FlashAppScreen.Items.name)
                        }
                    )
                }
                //lets create a route and destination
                composable(route = FlashAppScreen.Items.name) {
                    internetItemScreen(
                        flashViewModelItemScreen = flashViewmodel,
                        itemUiState = flashViewmodel.itemUiState
                    )
                }
                composable(route = FlashAppScreen.Cart.name) {
                    cartScreen(
                        flashViewModel = flashViewmodel,
                        onHomeButtonClicked = {
                            navController.navigate(FlashAppScreen.Start.name) {
                                popUpTo(0)
                            }
                        }
                    ) //CartScreen
                }
            }
        }
    }

}

@Composable
fun flashBottomAppBar(
    currentScreen: FlashAppScreen,
    navController: NavHostController,
    cartItemsCount: List<InternetApiItems>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 70.dp, vertical = 10.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {
                navController.navigate(FlashAppScreen.Start.name) {
                    popUpTo(0)
                }
            }
        ) {
            Icon(imageVector = Icons.Outlined.Home, contentDescription = "home icon")
            Text(text = "Home", fontSize = 10.sp)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.clickable {

                try {
                    if (currentScreen != FlashAppScreen.Cart) { // navigate to the cart, only when current screen is != to--
                        //-- CartScreen()
                        navController.navigate(FlashAppScreen.Cart.name) {
                            try {
                                popUpTo(0)
                            } catch (e: Exception) {
                                println(e.printStackTrace())
                            }
                        }
                    }
                } catch (e: Exception) {
                    println(e.printStackTrace())
                }
            }
        ) {
            Box {
                Icon(imageVector = Icons.Outlined.ShoppingCart, contentDescription = "Cart Icon")
                if (cartItemsCount.size > 0) {
                    Card(
                        modifier = Modifier.align(
                            alignment = Alignment.TopEnd
                        ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(
                            text = "${cartItemsCount.size}",
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 2.dp),
                            color = Color.White
                        )
                    }
                }
            }
            Text(text = "Cart", fontSize = 10.sp)
        }
    }
}