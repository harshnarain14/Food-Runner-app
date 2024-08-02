package com.example.fooddelivery.Activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.fooddelivery.Activity.Activity.util.ConnectionManager
import com.example.fooddelivery.Activity.Activity.util.Constraint
import com.example.fooddelivery.Activity.Activity.util.Preferences
import com.example.fooddelivery.R
import org.json.JSONObject
import java.lang.Exception
import java.util.HashMap

class signUp : AppCompatActivity() {
    lateinit var btnregister:Button
    lateinit var toolbar:Toolbar
    lateinit var etname:EditText
    lateinit var etemail:EditText
    lateinit var etmobile:EditText
    lateinit var etadd:EditText
    lateinit var etpass:EditText
    lateinit var etconpass:EditText
    private   lateinit var sharedPreferences: SharedPreferences
    private   lateinit var preferences:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        btnregister = findViewById(R.id.btnSubmit)
        etname = findViewById(R.id.etxtname)
        etemail = findViewById(R.id.etxtemail)
        etmobile = findViewById(R.id.etxtmobile)
        etadd = findViewById(R.id.etxtAdd)
        etpass = findViewById(R.id.etxtPassword)
        etconpass = findViewById(R.id.etxtCPassword)
        preferences = Preferences(this@signUp)
        sharedPreferences = this@signUp.getSharedPreferences(
            getString(R.string.prefrence_file_name),
            Context.MODE_PRIVATE
        )

        btnregister.setOnClickListener(View.OnClickListener {
            if (Constraint().validateNameLength(etname.text
                    .toString())) {
                etname.error = null // for showing a visual error
                if (Constraint().validateEmail(etemail.text.toString())) {
                    etemail.error = null
                    if (Constraint().validateMobile(etmobile.text.toString())) {
                        etmobile.error = null
                        if (Constraint().validatePasswordLength(etpass.text.toString())) {
                            etpass.error = null
                            if (Constraint().matchPassword(
                                    etpass.text.toString(),
                                    etconpass.text.toString()
                                )
                            ) {
                                etpass.error = null
                                etconpass.error = null
                                registerUser()

                            } else {
                                etpass.error = "Passwords don't match"
                                etconpass.error = "Passwords don't match"
                                Toast.makeText(
                                    this@signUp,
                                    "Passwords don't match",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        } else {
                            etpass.error = "Password should be more than or equal 4 digits"
                            Toast.makeText(
                                this@signUp,
                                "Password should be more than or equal 4 digits",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        etmobile.error = "Invalid Mobile number"
                        Toast.makeText(this@signUp, "Invalid Mobile number", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    etemail.error = "Invalid Email"
                    Toast.makeText(this@signUp, "Invalid Email", Toast.LENGTH_SHORT).show()
                }
            } else {
                etname.error = "Invalid Name"
                Toast.makeText(this@signUp, "Invalid Name", Toast.LENGTH_SHORT).show()
            }


        })

    }
    private fun registerUser(){

        val txtRegisterName = etname.getText().toString()
        val txtEmailAddress = etemail.getText().toString()
        val txtMobileNumber = etmobile.getText().toString()
        val txtDeliveryAddress = etadd.getText().toString()
        val txtRegPassword = etpass.getText().toString()

        if (ConnectionManager().isNetworkAvailable(this@signUp)){

            val queue = Volley.newRequestQueue(this@signUp)// volley is a library
            val url = "http://13.235.250.119/v2/register/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("name", txtRegisterName);
            jsonParams.put("mobile_number", txtMobileNumber)
            jsonParams.put("password", txtRegPassword)
            jsonParams.put("address", txtDeliveryAddress)
            jsonParams.put("email", txtEmailAddress)

            val jsonObjectRequest =
                object : JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {

                    try {

                        val registerJsonObject = it.getJSONObject("data")
                        val success = registerJsonObject.getBoolean("success")
                        if (success) {
                            val response = registerJsonObject.getJSONObject("data")
//                              here again we store the info in shareprefrences

                            sharedPreferences.edit()
                                .putString("user_id",response.getString("user_id")).apply()
                            sharedPreferences.edit()
                                .putString("user_name", response.getString("name")).apply()
                            sharedPreferences.edit()
                                .putString(
                                    "user_mobile_number",
                                    response.getString("mobile_number")
                                )
                                .apply()
                            sharedPreferences.edit()
                                .putString("user_address", response.getString("address"))
                                .apply()
                            sharedPreferences.edit()
                                .putString("user_email", response.getString("email")).apply()
                            preferences.setLogin(true)
                            val intent = Intent(baseContext, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            val message:String= registerJsonObject.getString("errorMessage")
                            Toast.makeText(
                                this@signUp,
                                message,
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }catch (e: Exception){
                        e.printStackTrace()
                    }
                }, Response.ErrorListener {
                    println("error is $it")

                    Toast.makeText(this@signUp,"Some error has occurred",Toast.LENGTH_SHORT).show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {

                        val headers = HashMap<String, String>()
                        headers["Content-Type"] = "application/json"
                        headers["token"] = "b312a26fe9bf61"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
            // if no internet connection found we display a toast message
        }else{
            Toast.makeText(this,"No Internet Connection found",Toast.LENGTH_SHORT).show()
        }
    }
    private  fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title="Registration "
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)// for displaying the default icon

    }
    // if the back icon on the toolbar is pressed
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if(id==android.R.id.home){
            val intent = Intent(baseContext, login_Activity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }

}

