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
import java.util.Date
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
        resetSteps()
        dbHelper = DatabaseHelper(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
            val dataString = "Date: "+ data.activtydate + " \n" + "Step It Up (no.of Steps): "+ data.stepitup.toString()+ " \n"+ "Points: "+ data.points.toString() + "\n-------------"
            userDataList.add(dataString)
        }
        myAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,userDataList)
        listView?.adapter = myAdapter
        bottomSheetDialog.show()
        bottomLayout.findViewById<Button>(R.id.btn_close).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
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
                sensorManager.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_GAME)
            }
            detectorSensor != null -> {
                sensorManager.registerListener(this,detectorSensor,SensorManager.SENSOR_DELAY_GAME)
            }
            accelerometer != null -> {
                sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME)
            }
            else ->{
                Toast.makeText(this,"No Sensor detected on this device",Toast.LENGTH_SHORT).show()
            }

        }
    }
    fun stepChecker(step: Int){
        if(totalSteps >= 100f){
            var cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
            val sharedPreferences = getSharedPreferences("steps",Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putFloat("previousTotalSteps",0f)
            editor.apply()

            cirbar.apply {  setProgressWithAnimation(0f) }
            Toast.makeText(this,"Congratulations you have completed the exercise", Toast.LENGTH_SHORT).show()
            val formatter = SimpleDateFormat("dd-MM-yyyy")
            val date = Date()
            val currentDate = formatter.format(date).toString()
            // Find if entry exist. If does exist for this user and today, update it to db else
            if(dbHelper.searchActivityRecords(username!!,currentDate).isEmpty()){
                // Create entry for today along with the points
                dbHelper.insertActivity(username!!,currentDate,100,step,0)
            }
            else{
                //Update db
                val currentData = dbHelper.searchActivityRecords(username!!,currentDate)[0]
                val newPoints = currentData.points + 100
                val newStepValue = currentData.stepitup + step
                dbHelper.updateActivityRecords(Constants.STEPITUP,currentDate,username!!,newPoints,newStepValue)
            }
            Toast.makeText(this,"Goal has been reached, resetting steps", Toast.LENGTH_LONG).show()
            val currentData = dbHelper.searchActivityRecords(username!!,currentDate)

            for(data in currentData){
                Log.d("STEP IT UP",data.stepitup.toString())
                Log.d("POINTS",data.points.toString())
            }
            var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
            stepsTakenTv.text = 0.toString()
            saveData()
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
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                stepsTaken.text = currentSteps.toString()
                stepChecker(currentSteps)
                cirbar.apply{
                    setProgressWithAnimation(currentSteps.toFloat())
                }

            }
        }
    }
    private fun saveData(){
        val sharedPreferences = getSharedPreferences("steps",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("previousTotalSteps",previousTotalSteps)
        editor.apply()
    }
    private fun loadData(){
        val sharedPreferences =getSharedPreferences("steps",Context.MODE_PRIVATE)
        val savedNumber: Float= sharedPreferences.getFloat("previousTotalSteps",0f)
        Log.d("StepActivity", "$savedNumber")
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        stepsTakenTv.text = savedNumber.toInt().toString()
    }
    private fun resetSteps(){
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        var cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
        stepsTakenTv.setOnClickListener { Toast.makeText(this,"Long tap to reset steps", Toast.LENGTH_SHORT).show()}
        stepsTakenTv.setOnLongClickListener {
            previousTotalSteps = totalSteps
            stepsTakenTv.text = 0.toString()
            cirbar.apply {setProgressWithAnimation(0f)  }
            saveData()
            true
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}