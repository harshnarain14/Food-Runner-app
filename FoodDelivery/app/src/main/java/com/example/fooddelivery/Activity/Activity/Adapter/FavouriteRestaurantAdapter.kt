package com.example.fooddelivery.Activity.Activity.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddelivery.Activity.Activity.DescriptionActivity
import com.example.fooddelivery.Activity.Activity.database.RestaurantEntity
import com.example.fooddelivery.Activity.Activity.fragments.AllRestaurants
import com.example.fooddelivery.Activity.Activity.fragments.FavoritesRestaurant
import com.example.fooddelivery.R
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso

class FavouriteRestaurantAdapter(
    val context: Context,
    private var itemList: List<RestaurantEntity>
) :
    RecyclerView.Adapter<FavouriteRestaurantAdapter.FavoriteViewHolder>() {
    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.txtRestrauntName)
        val restaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val pricePerPerson: TextView = view.findViewById(R.id.txtPrice)
        val restaurantRating: TextView = view.findViewById(R.id.txtRating)
        val favoriteRestaurant: ImageView = view.findViewById(R.id.imgFav)
        val cardView: MaterialCardView = view.findViewById(R.id.cardRestaurant)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.allrestaurants_recycler_single_row, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int
    ) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.restaurantName
        Picasso.get().load(restaurant.restaurantImageUrl).error(R.drawable.ic_baseline_error_24)
            .into(holder.restaurantImage);
        holder.pricePerPerson.text = "₹${restaurant.restaurantCostForOne}/person"
        holder.restaurantRating.text = restaurant.restaurantRating

        val checkFav = AllRestaurants.DBAsyncTask(context as Activity, restaurant, 1).execute()
        val isFav = checkFav.get()
        if (isFav) {
            holder.favoriteRestaurant.setImageResource(R.drawable.favourted)
        } else {
            holder.favoriteRestaurant.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
        holder.favoriteRestaurant.setOnClickListener {
            if (!AllRestaurants.DBAsyncTask(context, restaurant, 1).execute().get()) {
                val async = AllRestaurants.DBAsyncTask(context, restaurant, 2).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant Added To Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(
                        context,
                        "Some Error Occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                    notifyDataSetChanged()
                }
            } else {
                val async = AllRestaurants.DBAsyncTask(context, restaurant, 3).execute()
                val result = async.get()
                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant Removed From Favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                    val dbRestaurantList =
                        FavoritesRestaurant.RetrieveFavorites(context).execute().get()
                    itemList = dbRestaurantList
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
        holder.cardView.setOnClickListener {
            val intentToDescription = Intent(context, DescriptionActivity::class.java)
            intentToDescription.putExtra("restaurant_id", restaurant.restaurantId.toInt())
            intentToDescription.putExtra("restaurant_name", restaurant.restaurantName)
            intentToDescription.putExtra("restaurant_rating", restaurant.restaurantRating)
            intentToDescription.putExtra("restaurant_cost_for_one", restaurant.restaurantCostForOne)
            intentToDescription.putExtra("restaurant_image_url", restaurant.restaurantImageUrl)
            context.startActivity(intentToDescription)
        }
    }
}