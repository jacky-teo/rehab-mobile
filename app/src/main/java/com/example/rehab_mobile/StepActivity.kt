package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

class StepActivity : AppCompatActivity(), SensorEventListener {
    // Layout caller
    private lateinit var mainLayout: View
    private lateinit var bottomLayout: View

    private var magnitudePreviousStep: Double = 0.0
    private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int =100
    private var sensorManager: SensorManager? = null
    private var running: Boolean = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var username: String? = ""
    private lateinit var myAdapter: ArrayAdapter<String>
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = layoutInflater.inflate(R.layout.activity_step,null)
        bottomLayout = layoutInflater.inflate(R.layout.bottom_sheet,null)
        setContentView(R.layout.activity_step)
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")
        if(isPermissionGranted()){
            requestPermission()
        }
        loadData()
//        resetSteps()
        dbHelper = DatabaseHelper(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private fun requestPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
            ACTIVITY_RECOGNITION_REQUEST_CODE)
        }
    }
    private fun isPermissionGranted(): Boolean {
        // Check user have permissions enabled
        return ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            ACTIVITY_RECOGNITION_REQUEST_CODE ->{
                if(((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))){
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        running = true
        val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        val detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
        when{
            stepSensor != null -> {
                sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
            }
            detectorSensor != null -> {
                sensorManager.registerListener(this,detectorSensor,SensorManager.SENSOR_DELAY_UI)
            }
            accelerometer != null -> {
                sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_UI)
            }
            else ->{
                Toast.makeText(this,"No Sensor detected on this device",Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onPause(){
        super.onPause()
        running = false
        sensorManager?.unregisterListener(this)
    }
    override fun onSensorChanged(event: SensorEvent?) {
        var stepsTaken = findViewById<TextView>(R.id.stepsTaken)
        var cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            // we need to detect the magnitutede
            val xaccel: Float = event.values[0]
            val yaccel: Float = event.values[1]
            val zaccel: Float = event.values[2]
            val magnitude: Double = sqrt((xaccel*xaccel + yaccel *yaccel +zaccel*zaccel).toDouble())
            var magnitudeDelta: Double = magnitude-magnitudePreviousStep
            magnitudePreviousStep = magnitude
            if (magnitudeDelta > 6){
                totalSteps++
            }
            val step: Int = totalSteps.toInt()
            stepsTaken.text = step.toString()
            stepChecker(step)
            cirbar.apply {
                setProgressWithAnimation(step.toFloat())
            }
        }
        else{
            if (running){
                totalSteps = event!!.values[0]
                Log.d("totalSteps",totalSteps.toString()) //
                Log.d("Previous Total Steps",previousTotalSteps.toString())
//                val step: Int = totalSteps.toInt()
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                stepsTaken.text = currentSteps.toString()
//                stepsTaken.text = step.toString()
                stepChecker(currentSteps)
                cirbar.apply{
                    setProgressWithAnimation(currentSteps.toFloat())
                }
            }
        }
    }

    private fun loadData(){
        val sharedPreferences =getSharedPreferences("steps",Context.MODE_PRIVATE)
        val savedNumber: Float= sharedPreferences.getFloat("currentstep",0f)
        previousTotalSteps = savedNumber
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        stepsTakenTv.text = savedNumber.toInt().toString()
    }

    fun stepChecker(step: Int){
        if(step >= 100){
            Toast.makeText(this,"Congratulations you have completed the exercise", Toast.LENGTH_SHORT).show()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = calendar.time
            val formattedDate = dateFormat.format(currentDate)

            // Find if entry exist. If does exist for this user and today, update it to db else
            if(dbHelper.searchUserPoint(username!!).isEmpty()){
                dbHelper.insertPoint(username!!,0)
            }
//            if(dbHelper.searchActivityRecords(username!!,formattedDate).isEmpty()){
//                // Create entry for today along with the points
//                dbHelper.insertActivity(username!!,formattedDate,100,0)
//            }

                //Update db
                val currentData = dbHelper.searchActivityRecords(username!!,formattedDate)[0]
                val currentUserPoints = dbHelper.searchUserPoint(username!!)[0]
                val newPoints = currentUserPoints.points + 100
                val newStepValue = currentData.stepitup + step
                dbHelper.updateActivityRecords(Constants.STEPITUP,formattedDate,username!!,newStepValue)
                dbHelper.updateUserPoint(username!!,newPoints)

//            resetSteps()
        }
    }
    fun resetStepsBtn(view: View){
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        var cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
        stepsTakenTv.text = 0.toString()
        previousTotalSteps = totalSteps
        val sharedPreferences = getSharedPreferences("steps",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("currentstep",previousTotalSteps)
        editor.apply()
        cirbar.apply {setProgressWithAnimation(0f)  }
    }
    fun backToMainButton(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    fun viewStatistics(view: View) {
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(R.layout.bottom_sheet)
        val listView = bottomSheetDialog.findViewById<ListView>(R.id.activityInfoListView)
        val userActivities = dbHelper.searchUserActivityRecords(username!!)
        val userDataList = ArrayList<String>()
        for(data in userActivities){
            val dataString = "Date: "+ data.activtydate + " \n" + "Step It Up (no.of Steps): "+ data.stepitup.toString()+ "\n-------------"
            userDataList.add(dataString)
        }
        myAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,userDataList)
        listView?.adapter = myAdapter
        bottomSheetDialog.show()
//        bottomLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
    }
//    fun closeStatistics(view: View){
//        val bottomSheetDialog = BottomSheetDialog(this)
//        bottomSheetDialog.setContentView(R.layout.bottom_sheet)
//        bottomSheetDialog.dismiss()
//    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}