package com.example.fooddelivery.Activity.Activity.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val restaurantId:String,
    @ColumnInfo(name = "restaurant_name") val restaurantName: String,
    @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
    @ColumnInfo(name = "restaurant_cost_for_one") val restaurantCostForOne: String,
    @ColumnInfo(name = "restaurant_image") val restaurantImageUrl: String
)