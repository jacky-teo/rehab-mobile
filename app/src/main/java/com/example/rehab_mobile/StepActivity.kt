package com.example.rehab_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class StepActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var usernameEt: EditText
    private lateinit var activtydateEt: EditText
    private lateinit var pointsEt: EditText
    private lateinit var stepEt: EditText
    private lateinit var balanceEt: EditText

    private var username: String? = "admin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)
        dbHelper = DatabaseHelper(this)
        usernameEt = findViewById(R.id.username)
        activtydateEt = findViewById(R.id.activitydate)
        pointsEt = findViewById(R.id.points)
        stepEt = findViewById(R.id.stepactivity)
        balanceEt = findViewById(R.id.ballbalance)
    }
    fun getAllBtnClick(view: View){
        val allActivity = dbHelper.getAllActivityRecords()
        for (activity in allActivity){
            Log.d("username",activity.username)
            Log.d("date",activity.activtydate)
            Log.d("points",activity.points.toString())
            Log.d("step",activity.stepitup.toString())
            Log.d("ballbalance",activity.ballbalance.toString())
        }
    }

    fun getUserActivityBtnClick(view: View){
        val userActivty = dbHelper.searchActivityRecords("admin","28/2/2022")
        for (activity in userActivty){
            Log.d("username",activity.username)
            Log.d("date",activity.activtydate)
            Log.d("points",activity.points.toString())
            Log.d("step",activity.stepitup.toString())
            Log.d("ballbalance",activity.ballbalance.toString())
        }

    }

    fun createActivityBtnClick(view: View){
        val username = usernameEt.text.toString()
        val activtydate = activtydateEt.text.toString()
        val points = pointsEt.text.toString().toInt()
        val step = stepEt.text.toString().toInt()
        val balance = balanceEt.text.toString().toInt()
        val insert = dbHelper.insertActivity(username,activtydate,points,step,balance)
        val userActivty = dbHelper.searchActivityRecords(username,activtydate)
        for (activity in userActivty){
            Log.d("username",activity.username)
            Log.d("date",activity.activtydate)
            Log.d("points",activity.points.toString())
            Log.d("step",activity.stepitup.toString())
            Log.d("ballbalance",activity.ballbalance.toString())
        }
    }

    fun updateActivityBtnClick(view: View){
        val activtyName = "STEPITUP"
        val username = usernameEt.text.toString()
        val activtydate = activtydateEt.text.toString()
        val step = stepEt.text.toString().toInt()
        val points = pointsEt.text.toString().toInt()
        val update = dbHelper.updateActivityRecords(activtyName,activtydate,username,points,step)
        val userActivty = dbHelper.searchActivityRecords(username,activtydate)
        for (activity in userActivty){
            Log.d("username",activity.username)
            Log.d("date",activity.activtydate)
            Log.d("points",activity.points.toString())
            Log.d("step",activity.stepitup.toString())
            Log.d("ballbalance",activity.ballbalance.toString())
        }

    }
}