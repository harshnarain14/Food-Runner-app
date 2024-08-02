package com.example.fooddelivery.Activity.Activity.model

import org.json.JSONArray

data class OrderDetails(
    val orderId: String,
    val restaurantName: String,
    val orderPlacedAt: String,
    val totalCost: String,
    val foodItems: JSONArray
)
