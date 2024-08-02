package com.example.fooddelivery.Activity.Activity.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fooddelivery.Activity.Activity.database.MenuItemDao

@Database(
    entities = [RestaurantEntity::class, MenuItemEntity::class, OrderEntity::class],
    version = 1
)
abstract class RestaurantDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao
    abstract fun orderDao(): OrderDao
    abstract fun menuItemDao(): MenuItemDao
}