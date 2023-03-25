package com.example.rehab_mobile

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    private lateinit var btnDatePicker :Button
    var sex:String = ""
    var bloodType:String = ""
    private lateinit var user: FirebaseAuth
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // User Auth Instance
        user = FirebaseAuth.getInstance()

        dbHelper = DatabaseHelper(this)
        btnDatePicker = findViewById(R.id.dob)
        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, month)
            myCalendar.set(Calendar.DAY_OF_MONTH, day)
            updateLable(myCalendar)

        }
        btnDatePicker.setOnClickListener {
            DatePickerDialog(this, datePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }
        val sexSpinner = findViewById<Spinner>(R.id.sex)
        val bloodSpinner = findViewById<Spinner>(R.id.blood)
        sexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sex = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }
        bloodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                bloodType = parent?.getItemAtPosition(position).toString()
                // Do something with the selected item
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

    }
    private fun updateLable(myCalendar: Calendar){
        val myFormat = "dd-MM-yyyy"
        val sdf =SimpleDateFormat(myFormat, Locale.UK)
        btnDatePicker.hint = sdf.format(myCalendar.time)
    }

    fun cancelBtnClick(view: View) {
        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
    }
    fun registerSubmitClick(view: View) {
        val username = findViewById<EditText>(R.id.username).text.toString()
        val firstName = findViewById<EditText>(R.id.firstName).text.toString()
        val lastName = findViewById<EditText>(R.id.lastName).text.toString()
        val dob = findViewById<Button>(R.id.dob).hint.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val cfmPassword = findViewById<EditText>(R.id.confirmPassword).text.toString()
        if (password != cfmPassword){
            Toast.makeText( this,"Password mismatch, please retype your password", Toast.LENGTH_SHORT).show()
        }else if (username == null) {
            Toast.makeText( this,"Username is empty, please fill in a username", Toast.LENGTH_SHORT).show()

        }else if (firstName == null) {
            Toast.makeText( this,"Please fill in your first name", Toast.LENGTH_SHORT).show()

        }else if (lastName == null) {
            Toast.makeText( this,"Please fill in your last name", Toast.LENGTH_SHORT).show()

        }else if (password == null) {
            Toast.makeText( this,"Please fill in your password", Toast.LENGTH_SHORT).show()
        }else {

            //Register into firebase
            user.createUserWithEmailAndPassword(username,password).addOnCompleteListener(RegistrationActivity()){
                task ->
                if(task.isSuccessful){
                    dbHelper.insertUserInfo(username,firstName,lastName,dob,sex,bloodType)
                    val myIntent = Intent(this, MainActivity::class.java)
                    startActivity(myIntent)
                    Toast.makeText( this,"Successfully registered!", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText( this,task.exception!!.message, Toast.LENGTH_SHORT).show()
                }

            }

//            dbHelper.insertUserInfo(username,password,firstName,lastName,dob,sex,bloodType)
//            val myIntent = Intent(this, MainActivity::class.java)
//            startActivity(myIntent)
//            Toast.makeText( this,"Successfully registered!", Toast.LENGTH_SHORT).show()
        }
    }
}
