package com.example.fooddelivery.Activity

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.fooddelivery.Activity.ForgotPassword
import com.example.fooddelivery.R
import org.json.JSONObject
import com.example.fooddelivery.Activity.Activity.util.ConnectionManager
import com.example.fooddelivery.Activity.Activity.util.Constraint
import java.lang.Exception
import java.util.HashMap
import com.example.fooddelivery.Activity.Activity.util.Preferences

class login_Activity : AppCompatActivity() {
    private val APPLICATION_ID = "com.example.fooddelivery"
    private val PREFS_LOGIN_INSTANCE = "loginPref"
    lateinit var signup: TextView
    lateinit var signin: Button
    lateinit var forgotPass:TextView
    lateinit var mobilenumber:EditText
    lateinit var password:EditText
    lateinit var sharedPref:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)
        signup=findViewById(R.id.txtsignup)
        signin=findViewById(R.id.btnsignin)
        forgotPass=findViewById(R.id.txtforgotpass)
        mobilenumber=findViewById(R.id.mobileno)
        password=findViewById(R.id.password)
        sharedPref=getSharedPreferences(getString(R.string.prefrence_file_name),Context.MODE_PRIVATE)
        println(sharedPref)
            signin.setOnClickListener(View.OnClickListener {
                val txtLogMobileNumber :String= mobilenumber.getText().toString()
                val txtLogPassword: String= password.getText().toString()
                //  here we  have used the class COnstraints to check for validations
                if (Constraint().validateMobile(mobilenumber.text.toString()) && Constraint().validatePasswordLength(password.text.toString())) {

                    // here we are checking for internet connection
                    if (ConnectionManager().isNetworkAvailable(this@login_Activity)) {

                        val queue = Volley.newRequestQueue(this@login_Activity)// volley is a library
                        val url = "http://13.235.250.119/v2/login/fetch_result "
                        val jsonParams = JSONObject()




                        // these are the parameters for the post request

                        jsonParams.put("mobile_number", txtLogMobileNumber);
                        jsonParams.put("password", txtLogPassword)

                        val jsonObjectRequest =
                            object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                                try {
                                    val LoginJsonObject = it.getJSONObject("data")
                                    val success = LoginJsonObject.getBoolean("success")
                                    if (success) {

                                        //  if success then we save the credentials in sharedpreferences


                                        val response = LoginJsonObject.getJSONObject("data")
                                        sharedPref.edit()
                                            .putString("user_id", response.getString("user_id")).apply()
                                        sharedPref.edit()
                                            .putString("user_name", response.getString("name")).apply()
                                        sharedPref.edit()
                                            .putString(
                                                "user_mobile_number",
                                                response.getString("mobile_number")
                                            )
                                            .apply()
                                        sharedPref.edit()
                                            .putString("user_address", response.getString("address"))
                                            .apply()
                                        sharedPref.edit()
                                            .putString("user_email", response.getString("email")).apply()
                                        sharedPref.edit().putBoolean("login_prefs",true).apply()
                                        startActivity(
                                            Intent(
                                                this@login_Activity,
                                                MainActivity::class.java
                                            )
                                        )
                                        finish()


                                        // after saving we go to main or home activity
                                    } else {
                                        val message: String = LoginJsonObject.getString("errorMessage")
                                        Toast.makeText(
                                            this@login_Activity,
                                            "$message ",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }, Response.ErrorListener {
                                println("error is $it")

                                Toast.makeText(
                                    this@login_Activity,
                                    "Some error has occurred",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }) {
                                override fun getHeaders(): MutableMap<String, String> {

                                    val headers = HashMap<String, String>()
                                    headers["Content-Type"] = "application/json"
                                    headers["token"] = "b312a26fe9bf61"
                                    return headers
                                }
                            }
                        queue.add(jsonObjectRequest)
                    }else{

                        // if there is no internet connection available we show a alert dialog box

                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle(" Error ")
                        dialog.setMessage(" Internet Connection is not Found")
                        dialog.setPositiveButton("Open Settings"){text,listener->

                            // open settings
                            val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(settingsIntent)
                            finish()
                        }
                        dialog.setNegativeButton("Exit"){text, listener->
                            // this code is used to finish the app at any moment
                            ActivityCompat.finishAffinity(this)
                        }
                        dialog.create()
                        dialog.show()
                    }
                }else{
// if ther is some problems in validating
                    signin.visibility = View.VISIBLE
                    forgotPass.visibility = View.VISIBLE
                    signup.visibility = View.VISIBLE
                    Toast.makeText(this@login_Activity, "Invalid Number or Password", Toast.LENGTH_SHORT)
                        .show()

                }



            })






        println("hell")
        signup.setOnClickListener(View.OnClickListener {
            var intent= Intent(this@login_Activity,signUp::class.java)
            startActivity(intent)
            println("hello")

        })
        forgotPass.setOnClickListener(View.OnClickListener {
            var intent=Intent(this@login_Activity,ForgotPassword::class.java)
            startActivity(intent)
        })

    }
    override fun onPause() {
        super.onPause()
        finish()
    }

}

