package com.example.fooddelivery.Activity.Activity.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.core.view.isNotEmpty
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.fooddelivery.Activity.Activity.Adapter.AllRestaurantAdapter
import com.example.fooddelivery.Activity.Activity.Adapter.FavouriteRestaurantAdapter
import com.example.fooddelivery.Activity.Activity.database.RestaurantDatabase
import com.example.fooddelivery.Activity.Activity.database.RestaurantEntity
import com.example.fooddelivery.Activity.Activity.fragments.AllRestaurants
import com.example.fooddelivery.Activity.Activity.model.Restaurant
import com.example.fooddelivery.Activity.Activity.model.Restaurants
import com.example.fooddelivery.R

import com.google.android.material.snackbar.Snackbar


class FavoritesRestaurant : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: FavouriteRestaurantAdapter
    lateinit var progressBar: ProgressBar
    var dbRestaurantList = listOf<RestaurantEntity>()

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_favourite_restaurants, container, false)
        recyclerView = view.findViewById(R.id.recyclerFavouriteRestaurants)
        layoutManager = LinearLayoutManager(activity)
        progressBar = view.findViewById(R.id.progressBarfav)
        dbRestaurantList = RetrieveFavorites(activity as Context).execute().get()
        if (activity != null) {
            if (dbRestaurantList.isNotEmpty()) {
                progressBar.visibility = View.GONE
                recyclerAdapter =
                    FavouriteRestaurantAdapter(activity as Context, dbRestaurantList)
                recyclerView.adapter = recyclerAdapter
                recyclerView.layoutManager = layoutManager

            } else {
                progressBar.visibility = View.GONE
                Snackbar.make(view, "No Favorite Restaurants", Snackbar.LENGTH_SHORT)
                    .setAction("Go Back") {
                        requireActivity().onBackPressed()
                    }.show()
            }
        }

        return view
    }

    class RetrieveFavorites(val context: Context) :
        AsyncTask<Void, Void, List<RestaurantEntity>>() {
        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db")
                .build()
            return db.restaurantDao().getAllRestaurants()
        }
    }

    override fun onResume() {
        if (recyclerView.isNotEmpty()) {
            recyclerAdapter.notifyDataSetChanged()
        }
        super.onResume()
    }


}