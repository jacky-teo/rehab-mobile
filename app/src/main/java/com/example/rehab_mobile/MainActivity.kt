package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    //Login
    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEt = findViewById(R.id.username)
        passwordEt = findViewById(R.id.password)
        dbHelper = DatabaseHelper(this)
        // onCreate will check if the user has logged in before
        // If the user has logged in before, it will send them to Rehapp's default home page
        // Otherwise, it will show the login page
        val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("logged_in", false)
        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun verify(username: String, password: String): Boolean {
        if (username.isEmpty() || password.isEmpty()) {
            return false
        }
        val userData = HashMap<String, String>()
        // Check that user credentials are correct
        // Extract all records of user and returns if username and password match hashmap
        val userInfo = dbHelper.searchUserRecords(username)
        for (info in userInfo) {
            userData[info.username] = info.password
        }
        if (userData.containsKey(username)) {
            if (userData[username] == password) {
                return true
            }
            return false
        }
        return false
    }

    fun loginBtnClick(view: View) {
        val username = usernameEt.text.toString()
        val password = passwordEt.text.toString()

        if (verify(username, password)) {
            // True
            // 1. Save the login state so the user will not have to re-login in the future
            // 2. Redirect user to Rehapp's default home page
            val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("logged_in", true)
                editor.putString("username",username)
            editor.apply()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // False
            // 1. Alert the user that credentials are wrong
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
        }
    }

    fun registerBtnClick(view: View) {
        //TODO CREATE REGISTER PAGE
        val registrationIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(registrationIntent)
    }
//        val username = usernameEt.text.toString()
//        val password = passwordEt.text.toString()
//        if (username.isEmpty() && password.isEmpty()) {
//            Toast.makeText(this, "Invalid details", Toast.LENGTH_LONG).show()
//        } else {
//            val id = dbHelper.insertUserInfo(
//                username, password
//            )
//            Toast.makeText(this, "Information added successfully", Toast.LENGTH_LONG).show()
//
//        }
//    }
}