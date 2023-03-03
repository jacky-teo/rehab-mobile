package com.example.rehab_mobile

import android.content.Context
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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import org.w3c.dom.Text
import kotlin.math.sqrt

class StepActivity : AppCompatActivity(), SensorEventListener {
    private var magnitudePreviousStep: Double = 0.0
    private val ACTIVITY_RECOGNITION_REQUEST_CODE: Int =100
    private var sensorManager: SensorManager? = null
    private var running: Boolean = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step)

        if(isPermissionGranted()){
            requestPermission()
        }

        loadData()
        resetSteps()
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
            cirbar.apply {
                setProgressWithAnimation(step.toFloat())
            }
        }
        else{
            if (running){
                totalSteps = event!!.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                stepsTaken.text = currentSteps.toString()
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
        val savedNumber: Float= sharedPreferences.getFloat("currentStep",0f)
        Log.d("StepActivity", "$savedNumber")
        previousTotalSteps = savedNumber
    }
    private fun resetSteps(){
        var stepsTakenTv = findViewById<TextView>(R.id.stepsTaken)
        stepsTakenTv.setOnClickListener {
            Toast.makeText(this,"Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }
        stepsTakenTv.setOnLongClickListener {
            previousTotalSteps = totalSteps
            stepsTakenTv.text = 0.toString()
            saveData()
            true
        }
    }
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}