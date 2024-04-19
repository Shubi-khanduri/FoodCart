package com.example.foodcart.model

data class FoodItem(
    val name: String,
    val price: Double,
    var quantity: Int = 0
)
