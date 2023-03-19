package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.Slider

class BallInfoActivity : AppCompatActivity() {
    lateinit var sensitivityVal: NumberPicker
    private val ACCESS_FINE_LOCATION_REQUEST_CODE: Int =100
    private lateinit var myAdapter: ArrayAdapter<String>
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var bottomLayout: View
    private var username: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ball_info)
        sensitivityVal = findViewById<NumberPicker>(R.id.sensitivityVal)
        sensitivityVal.minValue = 1
        sensitivityVal.maxValue = 100
        sensitivityVal.value = 15
        if(isPermissionGranted()) {
            requestPermission()
        }
        dbHelper = DatabaseHelper(this)
        bottomLayout = layoutInflater.inflate(R.layout.bottom_sheet,null)
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")
    }

    fun startGameBtn(view: View) {
        val ballIntent = Intent(this, BallActivity::class.java)
        ballIntent.putExtra("sensitivity", "${sensitivityVal.value}")
        startActivity(ballIntent)
    }
    fun backBtn(view: View) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                ACCESS_FINE_LOCATION_REQUEST_CODE)
        }
    }
    private fun isPermissionGranted(): Boolean {
        // Check user have permissions enabled
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            ACCESS_FINE_LOCATION_REQUEST_CODE ->{
                if(((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))){
                }
            }
        }
    }

    fun displayStatistics(view: View) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet)
        val listView = bottomSheetDialog.findViewById<ListView>(R.id.activityInfoListView)
        val userActivities = dbHelper.searchBallUserActivityRecords(username!!)
        val userDataList = ArrayList<String>()
        for(data in userActivities){
            if (data.ballbalance == 0 ){
                val dataString = "Date: "+ data.activtydate + " \n" + "Score: "+ data.ballbalance.toString()+ "\n-------------"
                userDataList.add(dataString)
            }
        }
        myAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,userDataList)
        listView?.adapter = myAdapter
        bottomSheetDialog.show()
//        bottomLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
    }
}