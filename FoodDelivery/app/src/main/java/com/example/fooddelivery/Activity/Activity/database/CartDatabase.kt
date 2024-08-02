package com.example.fooddelivery.Activity.Activity.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.fooddelivery.Activity.Activity.database.FavresEntities
import com.example.fooddelivery.Activity.Activity.database.favresDao

@Database(entities = [CartEntities::class,FavresEntities::class], version = 1)
abstract class cartDatabase : RoomDatabase() {

     abstract fun favresDao(): favresDao

    abstract fun orderDao(): cartDao

}