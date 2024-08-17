package com.grocery.flash.Data

import androidx.annotation.StringRes
//import com.example.flash.R
import com.grocery.flash.R

object DataSource {
    fun loadCategories(): List<com.grocery.flash.Data.Categories> {
        var lst = mutableListOf<com.grocery.flash.Data.Categories>()
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.Sweet_tooth, imageResourceId = R.drawable.sweettooth
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.Stationary, imageResourceId = R.drawable.stationery
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.pet_food, imageResourceId = R.drawable.dogfoodistock
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.package_food,
                imageResourceId = R.drawable.healthypackagedfoods
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.munchies, imageResourceId = R.drawable.munchies
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.kitchen_essentials,
                imageResourceId = R.drawable.kitchenessentials
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                stringResourceId = R.string.vegetable, imageResourceId = R.drawable.freshveges
            )
        )
        lst.add(
            com.grocery.flash.Data.Categories(
                R.string.cleaning_essentials, //this kind of assigning parameter is known as position end parameter
                R.drawable.cleaningessentials
            )
        )
        return lst
    }

    fun loadItems(
        @StringRes categoryName:Int
    ): List<com.grocery.flash.Data.Item> {
        return listOf(
            com.grocery.flash.Data.Item(
                R.string.Apple,
                R.string.vegetable,
                "1KG",
                "150",
                R.drawable.apple
            ),
            com.grocery.flash.Data.Item(
                R.string.Mango,
                R.string.vegetable,
                "1 dozen",
                "300",
                R.drawable.mango
            ),
            com.grocery.flash.Data.Item(
                R.string.Banana,
                R.string.vegetable,
                "1 dozen",
                "30",
                R.drawable.banana
            ),
            com.grocery.flash.Data.Item(
                R.string.Orange,
                R.string.vegetable,
                "1KG",
                "200",
                R.drawable.orange
            ),
            com.grocery.flash.Data.Item(
                R.string.Pineapple,
                R.string.vegetable,
                "1KG",
                "120",
                R.drawable.pineapple
            ),
            com.grocery.flash.Data.Item(
                R.string.Pepsi1L,
                R.string.package_food,
                "1 L",
                "25",
                R.drawable.pepsi1l
            ),
        ).filter {
            it.itemCategoryId == categoryName
        }
    }
}