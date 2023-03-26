package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog

class StepInfoActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var bottomLayout: View
    private var username: String? = ""
    private lateinit var myAdapter: ArrayAdapter<String>
    private var totalMax = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)
        setContentView(R.layout.activity_step_info)
        bottomLayout = layoutInflater.inflate(R.layout.bottom_sheet,null)
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        val maxStepsTv = findViewById<EditText>(R.id.maxSteps)
        username = sharedPreference.getString("username","")

    }
    fun viewStatistics(view: View) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet)
        val listView = bottomSheetDialog.findViewById<ListView>(R.id.activityInfoListView)
        val userActivities = dbHelper.searchStepUserActivityRecords(username!!)
        val userDataList = ArrayList<String>()
        for(data in userActivities){
            val dataString = "Date: "+ data.activtydate + " \n" + "Step It Up (no.of Steps): "+ data.stepitup.toString()+ "\n-------------"
            userDataList.add(dataString)
        }
        myAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,userDataList)
        listView?.adapter = myAdapter
        bottomSheetDialog.show()
    }
    fun startGameBtn(view: View) {
        val maxStepsTv = findViewById<EditText>(R.id.maxSteps)
        if(maxStepsTv.text.toString() == ""){
            Toast.makeText(this,"Max steps cannot be empty", Toast.LENGTH_SHORT).show()
        }else{
            totalMax= maxStepsTv.text.toString().toInt()
            if(totalMax < 100){
                Toast.makeText(this,"You cannot have less than 100 steps", Toast.LENGTH_SHORT).show()
            }
            else if (totalMax %100!= 0){
                Toast.makeText(this,"Max steps must be in 100s", Toast.LENGTH_SHORT).show()
            }
            else{
                val stepIntent = Intent(this, StepActivity::class.java)
                stepIntent.putExtra("totalMax", "${totalMax}")
                startActivity(stepIntent)
            }
        }
    }
    fun backBtn(view: View) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }
}