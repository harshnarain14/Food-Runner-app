package com.example.fooddelivery.Activity.Activity.fragments

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import com.example.fooddelivery.R

class MyProfile : Fragment() {

    private lateinit var imgUserImage: ImageView
    private   lateinit var txtUserName: TextView
    private   lateinit var txtPhoneNumber: TextView
    private   lateinit var txtUserAddress:TextView
    private   lateinit var txtUserEmail: TextView
    private lateinit var profilePreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_my_profile, container, false)
        profilePreferences = (activity as FragmentActivity).getSharedPreferences(getString(R.string.prefrence_file_name), Context.MODE_PRIVATE)
        txtUserName=view.findViewById(R.id.userName)
        txtPhoneNumber=view.findViewById(R.id.userPhone)
        txtUserAddress=view.findViewById(R.id.postalAddress)
        txtUserEmail=view.findViewById(R.id.emailAddress)
        txtUserName.text = profilePreferences.getString("user_name", null)
        val phoneText = "+91-${profilePreferences.getString("user_mobile_number", null)}"
        txtPhoneNumber.text = phoneText
        txtUserEmail.text = profilePreferences.getString("user_email", null)
        val address = profilePreferences.getString("user_address", null)
        txtUserAddress.text = address
        println()

        return view
    }


}