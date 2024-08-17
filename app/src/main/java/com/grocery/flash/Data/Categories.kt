package com.grocery.flash.Data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Categories(
    @StringRes var stringResourceId : Int,
    @DrawableRes var imageResourceId:Int
)
