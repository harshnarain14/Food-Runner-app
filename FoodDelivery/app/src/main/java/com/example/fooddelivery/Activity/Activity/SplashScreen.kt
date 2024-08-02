package com.example.fooddelivery.Activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import com.example.fooddelivery.Activity.Activity.util.Preferences
import com.example.fooddelivery.R
import java.lang.Exception

class SplashScreen : AppCompatActivity() {
    lateinit var sharedPref: SharedPreferences
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        sharedPref =getSharedPreferences(getString(R.string.prefrence_file_name),
            Context.MODE_PRIVATE)
        imageView= findViewById(R.id.imageView)


        val background= object: Thread(){

            override fun run() {
                try {
                    sleep(5000)

                    /* here we are asking whether the user was logged in or out during his last work on app,if the user
                       logged out last time then, we go to login activity else we transit straight to main activity
                    */
                    if (sharedPref.getBoolean("shared_pref",false)) {
                        val intent = Intent(baseContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(baseContext, login_Activity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
    override fun onPause() {
        super.onPause()
        finish()

    }
}