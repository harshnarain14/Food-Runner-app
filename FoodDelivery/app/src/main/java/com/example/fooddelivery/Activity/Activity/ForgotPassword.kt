package com.example.fooddelivery.Activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.fooddelivery.Activity.Activity.ResetPasswordActivity
import com.example.fooddelivery.Activity.Activity.util.ConnectionManager
import com.example.fooddelivery.Activity.Activity.util.Constraint
import com.example.fooddelivery.R
import org.json.JSONObject

class ForgotPassword : AppCompatActivity() {
    private  lateinit var btnNext: Button
    private  lateinit var edtForgotMobile: EditText
    private  lateinit var edtForgotEmail: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnNext = findViewById(R.id.btnNext)
        edtForgotEmail = findViewById(R.id.edtForgotEmail)
        edtForgotMobile = findViewById(R.id.edtForgotMobile)

        btnNext.setOnClickListener {
            val forgotMobileNumber = edtForgotMobile.text.toString()
            if (Constraint().validateMobile(forgotMobileNumber)) {
                edtForgotMobile.error = null
                if (Constraint().validateEmail(edtForgotEmail.text.toString())) {
                    forgotPasswordOtp(edtForgotMobile.text.toString(), edtForgotEmail.text.toString())
                } else {
                    edtForgotEmail.error = "Invalid Email"
                }
            } else {
                edtForgotMobile.error = "Invalid Mobile Number"
            }
        }
    }


    fun forgotPasswordOtp(mobileNumber: String, emailAddress: String){
        if (ConnectionManager().isNetworkAvailable(this@ForgotPassword)) {
            val queue = Volley.newRequestQueue(this)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("email", emailAddress)

            val jsonObjectRequest =
                object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val firstTry = data.getBoolean("first_try")
                            if (firstTry) {
                                val builder = AlertDialog.Builder(this@ForgotPassword)
                                builder.setTitle("Information")
                                builder.setMessage("Please check your registered Email for the OTP.")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@ForgotPassword,
                                        ResetPasswordActivity::class.java
                                    )
                                    intent.putExtra("user_mobile", mobileNumber)
                                    startActivity(intent)
                                }
                                builder.create().show()
                            } else {
                                val builder = AlertDialog.Builder(this@ForgotPassword)
                                builder.setTitle("Information")
                                builder.setMessage("Please refer to the previous email for the OTP.")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@ForgotPassword,
                                        ResetPasswordActivity::class.java
                                    )
                                    intent.putExtra("user_mobile", mobileNumber)
                                    startActivity(intent)
                                }
                                builder.create().show()
                            }
                        } else {
                            //  rlContentMain.visibility = View.VISIBLE
                            // progressBar.visibility = View.GONE
                            Toast.makeText(
                                this@ForgotPassword,
                                "Mobile number not registered!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        //   rlContentMain.visibility = View.VISIBLE
                        // progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@ForgotPassword,
                            "Incorrect response error!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, Response.ErrorListener {
                    // rlContentMain.visibility = View.VISIBLE
                    // progressBar.visibility = View.GONE
                    VolleyLog.e("Error::::", "/post request fail! Error: ${it.message}")
                    Toast.makeText(this@ForgotPassword, it.message, Toast.LENGTH_SHORT)
                        .show()
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"

                        /*The below used token will not work, kindly use the token provided to you in the training*/
                        headers["token"] = "b312a26fe9bf61"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }else{
            Toast.makeText(this,"No Internet Connection found", Toast.LENGTH_SHORT).show()
        }
    }
}