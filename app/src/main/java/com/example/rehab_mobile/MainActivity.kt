package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    //Login
    private lateinit var usernameEt: EditText
    private lateinit var passwordEt: EditText
    private lateinit var user: FirebaseAuth

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.activity_main)

        usernameEt = findViewById(R.id.username)
        passwordEt = findViewById(R.id.password)
        dbHelper = DatabaseHelper(this)
        // onCreate will check if the user has logged in before
        // If the user has logged in before, it will send them to Rehapp's default home page
        // Otherwise, it will show the login page

        // Start instance of firebase auth
        user = FirebaseAuth.getInstance()
        val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("logged_in", false)
        if (isLoggedIn) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    fun loginBtnClick(view: View) {
        val username = usernameEt.text.toString()
        val password = passwordEt.text.toString()
        // Authentication user
        user.signInWithEmailAndPassword(username, password).
            addOnCompleteListener { task->
                if(task.isSuccessful){
                    val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("logged_in", true)
                    editor.putString("username",username)
                    editor.apply()
                    val intent = Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish()
                    // 1. Save the login state so the user will not have to re-login in the future
                    // 2. Redirect user to Rehapp's default home page
                }
                else{
                    Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }

    }

    fun registerBtnClick(view: View) {
        //TODO CREATE REGISTER PAGE
        val registrationIntent = Intent(this, RegistrationActivity::class.java)
        startActivity(registrationIntent)
    }
}