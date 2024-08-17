package com.grocery.flash.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
//import com.example.flash.Data.InternetApiItems
//import com.example.flash.R
import com.grocery.flash.Data.InternetApiItems
import com.grocery.flash.R
import java.math.BigDecimal

@Composable
fun ItemScreen(
    flashViewModelItemScreen: FlashViewModel,
    items: List<InternetApiItems>
) {
    val flashUiState by flashViewModelItemScreen.uiState.collectAsState()
    var selectedCategory = stringResource(id = flashUiState.selectedCategory)

    var database = items.filter {
        println("item cat${it.itemCategory.lowercase()}\n selected cat${selectedCategory.lowercase()}")
        it.itemCategory.lowercase() in selectedCategory.lowercase() ||
                "fruits" in it.itemCategory.lowercase() &&
                "fruits" in selectedCategory.lowercase()
    }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            Column {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(id = R.drawable.deliveryboy),
                    contentDescription = "delivery boy image",
                )
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0, 200, 0, 2)
                    )
                ) {
                    Text(
                        text = "${stringResource(id = flashUiState.selectedCategory)} (${database.size})",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        items(database) {
            itemCard(
                stringResourceId = it.itemName,
                imageResourceId = it.itemImage,
                itemQuantity = it.itemQty,
                itemPrice = it.itemPrice,
                flashViewModelItemScreen = flashViewModelItemScreen
            )

        }
    }
//    Text(text = stringResource(id = flashUiState.selectedCategory))
}

@Composable
fun internetItemScreen(
    flashViewModelItemScreen: FlashViewModel,
    itemUiState: FlashViewModel.ItemUiState
) {
    when (itemUiState) { 
        is FlashViewModel.ItemUiState.loading -> {
            loadingScreen()
        }

        is FlashViewModel.ItemUiState.Success -> {
            ItemScreen(
                flashViewModelItemScreen = flashViewModelItemScreen,
                items = itemUiState.item
                //next we have to make sure the itemScreen composable only display's --
                //-- the item which will be downloaded from internet
                //so create variable named database assign value item to it
            )
//            Text(text = itemUiState.item.toString(), modifier = Modifier.clickable {
//                println("${itemUiState.item.toString()}")
//            })
            // all downloaded data from api
        }

        else -> {
            errorScreen(flashViewModelItemScreen = flashViewModelItemScreen)
        }
    }
}

@Composable
fun itemCard(
    stringResourceId: String,
    imageResourceId: String,
    itemQuantity: String,
    itemPrice: Int,
    flashViewModelItemScreen: FlashViewModel
    ) {
    var context = LocalContext.current
    Column(modifier = Modifier.width(150.dp)) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(248, 221, 248, 255)
            )
        ) {
            Box {
                AsyncImage(
                    model = imageResourceId,
                    contentDescription = stringResourceId,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                    //above AsyncImage is for loading image from internet
                )

//                Image( //this traditional way o offline way to load image from drwable
//                    painter = painterResource(id = imageResourceId),
//                    contentDescription = "Image of Item",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(110.dp),
//                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.End
                ) {
                    Card(
                        colors = CardDefaults.cardColors(
                            contentColor = Color(244, 67, 54, 255)
                            //alpha is transparency
                        )
                    ) {
                        Text(
                            text = "25% OFF",
                            color = Color(255, 255, 255, 255),
                            fontSize = 8.sp,
                            modifier = Modifier.padding(3.dp)
                        )
                    }
                }
            }
        }
        Text(
            text = stringResourceId,
            fontSize = 12.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            maxLines = 1,
            textAlign = TextAlign.Start,
            style = TextStyle(
                color = Color.Green,
            )
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp)
                .background(Color.Yellow),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column() {
                Text(
                    text = "Rs. $itemPrice",
                    fontSize = 9.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color(109, 109, 109, 255),
                    textDecoration = TextDecoration.LineThrough
                )
                Text(
                    text = " Rs. ${
                        itemPrice.toBigDecimal() * BigDecimal(75) / BigDecimal(
                            100
                        )
                    }",
                    fontSize = 12.sp,
                    maxLines = 1,
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
            }
            Text(
                modifier = Modifier.padding(vertical = 4.dp),
                text = "${itemQuantity}",
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.Black
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .clickable {
                           flashViewModelItemScreen.addToFirebase(
                               InternetApiItems(
                                   itemName = stringResourceId,
                                   itemImage = imageResourceId,
                                   itemQty = itemQuantity,
                                   itemPrice = itemPrice,
                                   itemCategory = ""
                               )
                           )
                    Toast.makeText(context,"Added to Cart",Toast.LENGTH_SHORT).show()
                },
            colors = CardDefaults.cardColors(
                containerColor = Color(108, 194, 111, 255)
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "ADD TO CART", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun loadingScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.loading),
            contentDescription = "Loading screen"
        )
    }
}

@Composable
fun errorScreen(flashViewModelItemScreen: FlashViewModel) {

    Column (modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ){
        Text(
            text = "Internet Connection Problem",
            fontSize = 18.sp
            )
        Button(onClick = { flashViewModelItemScreen.getFlashItems()}) {
            Text(text = "Retry")
        }
    }

}