package com.example.rehab_mobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

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
        val nameTv = act!!.findViewById<TextView>(R.id.nameTv)
        val usernameTv = act!!.findViewById<TextView>(R.id.usernameTv)
        val birthdayTv = act!!.findViewById<TextView>(R.id.birthdayTv)
        val genderTv = act!!.findViewById<TextView>(R.id.genderTv)
        val bloodTypeTv = act!!.findViewById<TextView>(R.id.bloodTypeTv)
        val heightTv = act!!.findViewById<TextView>(R.id.heightTv)
        val weightTv = act!!.findViewById<TextView>(R.id.weightTv)
        val sharedPreference =requireActivity().getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")
        //Grab current user details
        val user = dbHelper.searchUserRecords(username!!)
        if(! user.isEmpty()){
            val userData = user[0]
            nameTv.text = userData.firstname + " " + userData.lastname
            usernameTv.text = userData.username
            birthdayTv.text = userData.dob
            genderTv.text = userData.sex
            bloodTypeTv.text = userData.bloodtype
            heightTv.text = userData.height + " m"
            weightTv.text = userData.weight + " kg"
        }
        else{
            Toast.makeText(act!!,"No User Data", Toast.LENGTH_SHORT).show()
        }



    }
}