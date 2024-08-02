package com.example.fooddelivery.Activity.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.fooddelivery.Activity.login_Activity
import com.example.fooddelivery.R
import org.json.JSONObject
import com.example.fooddelivery.Activity.Activity.util.Constraint
import com.example.fooddelivery.Activity.Activity.util.ConnectionManager

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var edtOTP: EditText
    private lateinit var edtNewPassword: EditText
    private lateinit var edtConfirmNewPassword: EditText
    private lateinit var btnSubmit: Button
    private var mobileNumber: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)
        edtOTP = findViewById(R.id.edtotp)
        edtConfirmNewPassword = findViewById(R.id.edtnewconpass)
        edtNewPassword = findViewById(R.id.edtnewpass)
        btnSubmit = findViewById(R.id.btnnewsubmit)

        if (intent != null) {
            mobileNumber = intent.getStringExtra("user_mobile").toString()
        }

        btnSubmit.setOnClickListener {

            // again we nested if statements for the validations
            if (edtOTP.text.length == 4) {
                if (Constraint().validatePasswordLength(edtNewPassword.text.toString())) {
                    if (Constraint().matchPassword(
                            edtNewPassword.text.toString(),
                            edtConfirmNewPassword.text.toString()
                        )
                    ) {
                        sendOtp(
                            mobileNumber,
                            edtOTP.text.toString(),
                            edtNewPassword.text.toString()
                        )
                    } else {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "Passwords do not match",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Invalid Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this@ResetPasswordActivity, "Incorrect OTP", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
    private  fun sendOtp(mobileNumber:String,otp:String,newPassword:String) {
        if (ConnectionManager().isNetworkAvailable(this@ResetPasswordActivity)) {
            val queue = Volley.newRequestQueue(this)
            val resetUrl = "http://13.235.250.119/v2/reset_password/fetch_result"

            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", mobileNumber)
            jsonParams.put("password", newPassword)
            jsonParams.put("otp", otp)

            val jsonObjectRequest =
                object :
                    JsonObjectRequest(Request.Method.POST, resetUrl, jsonParams, Response.Listener {
                        try {
                            val resetObject = it.getJSONObject("data")
                            val success = resetObject.getBoolean("success")
                            if (success) {
                                val builder = AlertDialog.Builder(this@ResetPasswordActivity)
                                builder.setTitle("Confirmation")
                                builder.setMessage("Your password has been changed successfully")
                                builder.setIcon(R.drawable.success)
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    startActivity(
                                        Intent(
                                            this@ResetPasswordActivity,
                                            login_Activity::class.java
                                        )
                                    )
                                    ActivityCompat.finishAffinity(this@ResetPasswordActivity)
                                }
                                builder.create().show()
                            } else {
                                val error = resetObject.getString("errorMessage")
                                Toast.makeText(
                                    this@ResetPasswordActivity,
                                    error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Incorrect Response!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        VolleyLog.e("Error::::", "/post request fail! Error: ${it.message}")
                        Toast.makeText(this@ResetPasswordActivity, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Content-type"] = "application/json"
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