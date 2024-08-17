package com.grocery.flash.Data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InternetApiItems(
//it throws error during fetching record from RealTimeDb
    @SerialName(value = "stringResourceId")
    val itemName:String ="",
    @SerialName(value = "itemCategoryId")
    val itemCategory:String="",
    @SerialName(value = "itemQuantity")
    val itemQty:String="",
    @SerialName(value = "item_price")
    val itemPrice:Int=0,
    @SerialName(value = "imageResourceId")
    val itemImage:String=""
)
