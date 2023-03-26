package com.example.rehab_mobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class User : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    private var username: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity
        dbHelper = DatabaseHelper(requireActivity())
        val userNameTv = act!!.findViewById<TextView>(R.id.userName)
        val sharedPreference =requireActivity().getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")
        //Grab current user details
        val user = dbHelper.searchUserRecords(username!!)
        Log.d("user", user.toString())
        userNameTv.text = username

    }
}