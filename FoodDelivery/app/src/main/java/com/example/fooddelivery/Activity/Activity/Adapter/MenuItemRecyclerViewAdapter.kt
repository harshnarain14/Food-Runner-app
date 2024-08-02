package com.example.fooddelivery.Activity.Activity.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.Activity.Activity.DescriptionActivity
import com.example.fooddelivery.Activity.Activity.database.MenuItemEntity
import com.example.fooddelivery.Activity.Activity.model.MenuItem
import com.example.fooddelivery.R
import com.google.android.material.button.MaterialButton

class MenuItemRecyclerViewAdapter(
    val context: Context,
    private val itemList: ArrayList<MenuItem>,
    private val listener: OnItemClickListener
) :

    RecyclerView.Adapter<MenuItemRecyclerViewAdapter.MenuItemViewHolder>() {


    class MenuItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idTextView: TextView = view.findViewById(R.id.serial_number)
        val itemName: TextView = view.findViewById(R.id.item_name)
        val itemCostForOne: TextView = view.findViewById(R.id.item_cost_for_one)
        val addItemButton: MaterialButton = view.findViewById(R.id.add_item_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclermenuiter, parent, false)
        return MenuItemViewHolder(view)
    }

    interface OnItemClickListener {
        fun onAddItemClick(menuItem: MenuItem)
        fun onRemoveItemClick(menuItem: MenuItem)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuItemViewHolder, position: Int) {
        val menuItem = itemList[position]
        holder.idTextView.text = (position + 1).toString()
        holder.itemName.text = menuItem.itemName
        holder.itemCostForOne.text = "₹ ${menuItem.itemCostForOne}"

        val menuItemEntity = MenuItemEntity(
            menuItem.itemId,
            menuItem.itemName,
            menuItem.itemCostForOne,
            menuItem.restaurantId
        )
        val checkAdded =
            DescriptionActivity.DBAsyncTask(context as Activity, menuItemEntity, 1).execute()
        val isAdded = checkAdded.get()
        if (isAdded) {
            holder.addItemButton.backgroundTintList = ContextCompat.getColorStateList(
                context,
                R.color.button
            )
            holder.addItemButton.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.buttonON
                )
            )
            holder.addItemButton.text = "Remove"
        } else {
            holder.addItemButton.backgroundTintList = ContextCompat.getColorStateList(
                context,
                R.color.button
            )
            holder.addItemButton.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.buttonON
                )
            )
            holder.addItemButton.text = "Add"
        }


        holder.addItemButton.setOnClickListener {
            if (!DescriptionActivity.DBAsyncTask(context, menuItemEntity, 1).execute().get()) {
                val async = DescriptionActivity.DBAsyncTask(context, menuItemEntity, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Item Added To Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    listener.onAddItemClick(menuItem)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context as Activity,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    notifyDataSetChanged()
                }
            } else {
                val async = DescriptionActivity.DBAsyncTask(context, menuItemEntity, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context as Activity,
                        "Item Removed From Cart",
                        Toast.LENGTH_SHORT
                    ).show()
                    listener.onRemoveItemClick(menuItem)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    notifyDataSetChanged()
                }
            }
        }
    }
}