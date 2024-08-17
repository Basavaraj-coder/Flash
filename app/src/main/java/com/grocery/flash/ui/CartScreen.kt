package com.grocery.flash.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.grocery.flash.Data.InternetApiItems
import com.grocery.flash.Data.InternetItemWithQty
import com.grocery.flash.R
import com.grocery.flash.ui.theme.Pink0


@Composable
fun cartScreen(
    flashViewModel: FlashViewModel,
    onHomeButtonClicked: () -> Unit
) {
    val cartItems by flashViewModel.cartItem.collectAsState()
    val cartItemsWithQuantity = cartItems.groupBy { it }.map { (item, cartItems) ->
        InternetItemWithQty(
            item, cartItems.size
        )
    }

    if (cartItems.isNotEmpty()) {
        LazyColumn(
            contentPadding = PaddingValues(horizontal = 3.dp), //this padding is for whole column
            verticalArrangement = Arrangement.spacedBy(10.dp),// this padding is for space between views available
            horizontalAlignment = Alignment.CenterHorizontally
            //-- inside lazyColumn
        ) {
            item {
                Row(modifier = Modifier.padding(horizontal = 50.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.deliveryboy),
                        contentDescription = "Cart image",
                    )
                }
            }
            item {
                Text(text = "Review Items/Cart Items", fontSize = 18.sp)
            }
            items(cartItemsWithQuantity) {
                cartCard(it.item, flashViewModel, it.quantity)
            }
            //billing details
            val price = cartItems.sumOf {
                it.itemPrice * 75 / 100
            }
            val handlingCharge = price * 1 / 100
            val deliveryCharge = 30
            val total = price + handlingCharge + deliveryCharge
            item {
                Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())
                Text(text = "Bill Details", modifier = Modifier.padding(vertical = 8.dp))
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(200, 200, 200, 255)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 3.dp, end = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) { // billing card
                        // Text(text = "Hello")

                        billRow(
                            itemName = "Item Total",
                            itemPrice = price,
                            fontWeight = FontWeight.Normal
                        )
                        billRow(
                            itemName = "Handling Charges",
                            itemPrice = handlingCharge,
                            fontWeight = FontWeight.Normal
                        )
                        billRow(
                            itemName = "Delivery Charges",
                            itemPrice = deliveryCharge,
                            fontWeight = FontWeight.Normal
                        )
                        Divider(
                            thickness = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(3.dp),
                            color = Color.LightGray
                        )
                        billRow(
                            itemName = "Grand Total",
                            itemPrice = total,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Pink0),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_remove_shopping_cart_24),
                contentDescription = "Empty cart",
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = "Cart is Empty",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            FilledTonalButton(onClick = { onHomeButtonClicked() }) {
                Text(text = "Browse Products")
            }
        }
    }
}

@Composable
fun cartCard(cartItem: InternetApiItems, flashViewModel: FlashViewModel, reducedCartItemQty: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
//        Column(modifier = Modifier
//            .fillMaxHeight()
//            .padding(5.dp)) {
//
//
//            //Text(text = "Images")
////
//        }
//        Image(
//            painter = painterResource(id = R.drawable.apple), contentDescription = "Apple image"
//        )
        AsyncImage(
            modifier = Modifier
                .width(80.dp)
                .fillMaxHeight(),
            // .weight(2f),//weight is like how many components are available in a row including his we have 4 compo
            model = cartItem.itemImage,
            contentDescription = "sample image",
        )
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = cartItem.itemName,
                fontSize = 12.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
            Text(
                text = cartItem.itemQty,
                fontSize = 14.sp,
                maxLines = 1,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Rs ${cartItem.itemPrice}",
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.LightGray,
                textDecoration = TextDecoration.LineThrough,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Rs ${cartItem.itemPrice * 75 / 100}",
                fontSize = 18.sp,
                maxLines = 1,
                color = Color.Blue,
                textAlign = TextAlign.Center
            )
        }
        Column(
            modifier = Modifier
                .padding(horizontal = 15.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceEvenly,

            ) {
            Text(
                text = reducedCartItemQty.toString(),
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.Black,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
//            Card(
//                modifier = Modifier
//                    .size(width = 65.dp, height = 30.dp)
//                    .clickable {
//
//                    }, colors = CardDefaults.cardColors(containerColor = Color.Red)
//            ) {
//                Text(
//                    text = "Remove",
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    textAlign = TextAlign.Center
//                )
//            }
            Button(modifier = Modifier
                .fillMaxWidth()
                .weight(4f),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                onClick = {
                    flashViewModel.removeFromCart(cartItem)
                }) {
                Image(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "delete/remove icon"
                )
            }
        }
    }
    Divider(thickness = 3.dp, modifier = Modifier.fillMaxWidth())
}

@Composable
fun billRow(
    itemName: String, itemPrice: Int, fontWeight: FontWeight
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(text = itemName, fontWeight = fontWeight)
        Text(text = "Rs $itemPrice", fontWeight = fontWeight)
    }
}
