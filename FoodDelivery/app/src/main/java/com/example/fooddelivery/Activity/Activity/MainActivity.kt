package com.example.fooddelivery.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.toolbox.Volley
import com.example.fooddelivery.Activity.Activity.fragments.*
import com.example.fooddelivery.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private var previousMenuItem: MenuItem? = null
    private lateinit var preferences: com.example.fooddelivery.Activity.Activity.util.Preferences
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = com.example.fooddelivery.Activity.Activity.util.Preferences(this@MainActivity)
        sharedPrefs = this@MainActivity.getSharedPreferences(
            getString(R.string.prefrence_file_name),w
            Context.MODE_PRIVATE
        )


        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar= findViewById(R.id.toolbar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        setUpToolbar()
        openAllRestraunts()
// for creating a toogle icon
        val actionBarDrawerToggle = ActionBarDrawerToggle(this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
// this is done to check the fragment which the user is cuurently working at
            if(previousMenuItem != null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem = it

            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)

// thw following code is for transfer of a fragment to another fragment
            when(it.itemId){
                R.id.menuHome -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                    R.id.frameLayout,
                            AllRestaurants()|
}"commit()
                    supportActionBar?.title=" All Restaurants"
                    drawerLayout.closeDrawers()
                }
                R.id.menuMyProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            MyProfile()
                        ).commit()
                    supportActionBar?.title="My Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.menufavouriteRestaurants-> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,FavoritesRestaurant()
                        ).commit()
                    supportActionBar?.title="My Favourite Restraunts"
                    drawerLayout.closeDrawers()
                }
                R.id.menuOrderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            OrderHistory()
                        ).commit()
                    supportActionBar?.title="My order history"
                    drawerLayout.closeDrawers()
                }
                R.id.menuLogout->{
//                  if the user wishes to log out we show a alert dialog box
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("Confirmation")
                        .setMessage("Are you sure you want exit?")
                        .setPositiveButton("Yes") { _, _ ->
                            preferences.setLogin(false)
                            sharedPrefs.edit().clear().apply()
                            startActivity(Intent(this@MainActivity, login_Activity::class.java))
                            Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                            ActivityCompat.finishAffinity(this)
                        }
                        .setNegativeButton("No") { _, _ ->
                            openAllRestraunts()
                        }
                        .create()
                        .show()
                    drawerLayout.closeDrawers()
                }
                R.id.menuFAQs-> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            FAQs()
                        ).commit()
                    supportActionBar?.title="Frequently Asked Questions?"
                    drawerLayout.closeDrawers()
                }
            }
            return@setNavigationItemSelectedListener true
        }

        // the following code is used to add header to the navigation view
        // we have not dine that in xml code because to add listeners to img and nametext
        // learnt online this method
        val headerView = LayoutInflater.from(this@MainActivity).inflate(R.layout.header, null)
        val txtHeaderName: TextView = headerView.findViewById(R.id.txtHeaderName)

        txtHeaderName.text = sharedPrefs.getString("user_name", null)
        val txtHeaderMobile: TextView =  headerView.findViewById(R.id.txtHeaderMobile)
        val imgPersonHeader: ImageView = headerView.findViewById(R.id.imgPersonHeader)
        val phoneText = "+91-${sharedPrefs.getString("user_mobile_number", null)}"
        txtHeaderMobile.text = phoneText
        navigationView.addHeaderView(headerView)

        // if the user clicks on the name of the user we transit to my profile fragment
        txtHeaderName.setOnClickListener {
            val myProfile = MyProfile()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, myProfile)
            transaction.commit()
            supportActionBar?.title = "My profile"
            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)
            navigationView.setCheckedItem(R.id.menuMyProfile)
        }
//      same with the case of the img
        imgPersonHeader.setOnClickListener {
            val myProfile = MyProfile()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, myProfile)
            transaction.commit()
            supportActionBar?.title = "My profile"
            val mPendingRunnable = Runnable { drawerLayout.closeDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)
            navigationView.setCheckedItem(R.id.menuMyProfile)
        }
    }
    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = " Toolbar Title "
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)// for displaying the default icon
    }


    // this function for showing what happens when a toggle icon is pressed which is present at the menu bar
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id==android.R.id.home){

//      learnt online to delay the functionality which is good practice for making motions smooth

            val mPendingRunnable = Runnable { drawerLayout.openDrawer(GravityCompat.START) }
            Handler().postDelayed(mPendingRunnable, 100)
        }
        return super.onOptionsItemSelected(item)
    }


    // function used so that whenever we want to open the restaurants fragments we can do so using a function
    private fun openAllRestraunts(){
        val fragment = AllRestaurants()
        val fragmentManager = supportFragmentManager.beginTransaction()// supportFragmentManager is used as intent
        fragmentManager.replace(R.id.frameLayout,fragment)
        fragmentManager.commit()
        supportActionBar?.title=" All Restaurants"
        navigationView.setCheckedItem(R.id.menuHome)// for showing the shade of the item selected in navigation view
    }
    // function which decides the functionality of the back button
    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)
        when(frag) {
            !is AllRestaurants -> openAllRestraunts()
            else -> {
                Volley.newRequestQueue(this).cancelAll(this::class.java.simpleName)
                ActivityCompat.finishAffinity(this@MainActivity)
            }
        }
    }



}