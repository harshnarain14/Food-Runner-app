package com.example.fooddelivery.Activity.Activity.model

import org.json.JSONArray

data class orderItemDetails(
    val orderId: Int,
    val resName: String,
    val orderDate: String,
    val foodItems: JSONArray
)