package com.grocery.flash.Data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Item(
    @StringRes val stringResourceId : Int,
    @StringRes val itemCategoryId :Int,
    val itemQuantity:String,
    val itemPrice :String,
    @DrawableRes val imageResourcesId:Int
)
