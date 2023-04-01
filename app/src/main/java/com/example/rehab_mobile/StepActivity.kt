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
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

class StepActivity : AppCompatActivity(), SensorEventListener {
    // Layout caller
    private lateinit var mainLayout: View
//    private lateinit var bottomLayout: View

    private var magnitudePreviousStep: Double = 0.0
    private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int =100
    private var sensorManager: SensorManager? = null
    private var running: Boolean = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private var currentSteps = 0
    private var username: String? = ""

    private var totalMax = 0
    private lateinit var dbHelper: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainLayout = layoutInflater.inflate(R.layout.activity_step,null)


        setContentView(R.layout.activity_step)
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")
        if(isPermissionGranted()){
            requestPermission()
        }
        val cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)

        dbHelper = DatabaseHelper(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        totalMax = intent.getStringExtra("totalMax")!!.toInt()
        cirbar.progressMax = totalMax.toFloat()
        cirbar.apply {
            setProgressWithAnimation(0.toFloat())
        }
        stepsTakenTv.text= "0"

        val startButton = findViewById<Button>(R.id.resetBtn)
        startButton.visibility = View.VISIBLE

        Toast.makeText(this,"Please Press Reset To Start Exercise", Toast.LENGTH_SHORT).show()
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
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        var cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
        var totalMaxTv = findViewById<TextView>(R.id.totalMax)
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
            stepsTakenTv.text = step.toString()
            stepChecker(step)
            cirbar.apply {
                setProgressWithAnimation(step.toFloat())
            }
        }
        else{
            if (running){
                totalSteps = event!!.values[0]
                currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                Log.d("Running Event Listener Current Steps", currentSteps.toString())
                if(stepChecker(currentSteps)){
                    stepsTakenTv.text= "${totalMax}"
                    Toast.makeText(this,"Activity Completed", Toast.LENGTH_SHORT).show()
                    running = false
                    sensorManager?.unregisterListener(this)
                    finish()
                }
                else{
                    totalMaxTv.text = "/ ${totalMax}"
                    stepsTakenTv.text = currentSteps.toString()
                    cirbar.apply{
                        setProgressWithAnimation(currentSteps.toFloat())
                    }
                    if(currentSteps > totalMax){
                        cirbar.apply{
                            setProgressWithAnimation(currentSteps.toFloat())
                        }
                        Toast.makeText(this,"Please press start to begin exercise", Toast.LENGTH_SHORT).show()
                        stepsTakenTv.text = ""
                        totalMaxTv.text = ""
                        running = false
                    }

                }

            }
        }
    }

    fun stepChecker(step: Int) : Boolean{
        if(step == totalMax || step == totalMax +1 || step == totalMax +2 ){ // Added this to adjust tot the inaccuracry of the sensor
            Toast.makeText(this,"Congratulations you have completed the exercise", Toast.LENGTH_SHORT).show()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val currentDate = calendar.time
            val formattedDate = dateFormat.format(currentDate)
            // Find if entry exist. If does exist for this user and today, update it to db else
            if(dbHelper.searchUserPoint(username!!).isEmpty()){
                dbHelper.insertPoint(username!!,0)
            }
            //Update db
            val currentData = dbHelper.searchStepActivityRecords(username!!,formattedDate)[0]
            val currentUserPoints = dbHelper.searchUserPoint(username!!)[0]
            Log.d("User data", "${currentUserPoints.username} : ${currentUserPoints.points}")
            val newPoints = currentUserPoints.points  + 100000
//            + (step*0.1).toInt()
            val newStepValue = currentData.stepitup + totalMax
            dbHelper.updateStepActivityRecords(formattedDate,username!!,newStepValue)
            dbHelper.updateUserPoint(username!!,newPoints)
            previousTotalSteps = 0f
            totalSteps = 0f
            currentSteps = 0
            return true
        }
        return false
    }
    fun resetStepsBtn(view: View){
        val stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        val totalMaxTv = findViewById<TextView>(R.id.totalMax)
        val cirbar = findViewById<CircularProgressBar>(R.id.progress_circular)
        val startButton = findViewById<Button>(R.id.resetBtn)
        startButton.visibility = View.INVISIBLE
        stepsTakenTv.text = 0.toString()
        totalMaxTv.text = "/ ${totalMax}"
        previousTotalSteps = totalSteps
        val sharedPreferences = getSharedPreferences("steps",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putFloat("currentstep",previousTotalSteps)
        editor.apply()
        cirbar.apply {setProgressWithAnimation(0f)  }
        running = true
    }
    fun backToMainButton(view: View){
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }


}