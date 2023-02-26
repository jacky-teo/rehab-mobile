package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // onCreate will check if the user has logged in before
        // If the user has logged in before, it will send them to Rehapp's default home page
        // Otherwise, it will show the login page
        val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("logged_in", false)
        if (isLoggedIn) {
            val intent = Intent(this, RehappMainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun verify(username: String, password: String): Boolean {
        // Check that user credentials are correct
        // Placeholder (true) for now until database has been setup
        return true
    }
    fun loginBtnClick(view: View) {
        val username = findViewById<EditText>(R.id.username).toString()
        val password = findViewById<EditText>(R.id.password).toString()
        if(verify(username, password)) {
            // True
            // 1. Save the login state so the user will not have to re-login in the future
            // 2. Redirect user to Rehapp's default home page
            val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("logged_in", true)
            editor.apply()
            val intent = Intent(this, RehappMainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // False
            // 1. Alert the user that credentials are wrong
            Toast.makeText(this, "Invalid Login Details", Toast.LENGTH_SHORT).show()
        }
    }
}