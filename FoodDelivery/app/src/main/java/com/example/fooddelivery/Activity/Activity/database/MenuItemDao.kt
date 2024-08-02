package com.example.fooddelivery.Activity.Activity.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.fooddelivery.Activity.Activity.database.MenuItemEntity

@Dao
interface MenuItemDao {
    @Insert
    fun insertMenuItem(menuItemEntity: MenuItemEntity)

    @Delete
    fun deleteMenuItem(menuItemEntity: MenuItemEntity)

    @Query("SELECT * FROM menuItems")
    fun getAllMenuItems(): List<MenuItemEntity>

    @Query("SELECT * FROM menuItems WHERE menuItemId = :menuItemId")
    fun getMenuItemById(menuItemId: String): MenuItemEntity

    @Query("DELETE FROM menuItems")
    fun deleteAllMenuItems()

}