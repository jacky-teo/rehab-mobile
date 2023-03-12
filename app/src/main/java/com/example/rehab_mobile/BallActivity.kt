package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class BallActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var timer: Timer? = null
    private var elapsedTime = 0L
    private var sensitivity = 15
    private lateinit var dbHelper: DatabaseHelper
    private var username: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ball)
        setUpSensorStuff()
        setUpTimer()
        dbHelper = DatabaseHelper(this)
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")

    }
    private fun setUpSensorStuff(){
        sensitivity = intent.getStringExtra("sensitivity")!!.toInt()
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }
    override fun onSensorChanged(p0: SensorEvent?) {
        if(p0?.sensor?.type == Sensor.TYPE_ACCELEROMETER){
            val sides = p0.values[0]
            val upDown = p0.values[1]
            val circle = findViewById<FloatingActionButton>(R.id.circle)

            circle.x -= (sides * sensitivity)
            circle.y += (upDown * sensitivity)

            if (circle.x < (0 - circle.width) || circle.x > getScreenWidth(this)){
                gameOver()
            }
            if (circle.y < (0 - circle.width) || circle.y > (getScreenHeight(this) - 225)){
                gameOver()
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }
    private fun gameOver(){
        val minutes = (elapsedTime / 1000) / 60
        val seconds = (elapsedTime / 1000) % 60

        //Save to DB
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val currentDate = formatter.format(date).toString()
        val points = ((minutes * 80) + (seconds * 1))*(sensitivity/15)
        dbHelper.insertActivity(username!!,currentDate,0,elapsedTime.toInt())

        //Check if username has points entry
        var userPoint = dbHelper.searchUserPoint(username!!)


        if(userPoint.isEmpty()){
            dbHelper.insertPoint(username!!,points.toInt())
        }
        else{
            val currentPoint = dbHelper.searchUserPoint(username!!)[0].points
            val newpoints = currentPoint + points.toInt()
            dbHelper.updateUserPoint(username!!,newpoints)
        }

        //Setting up pop up
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game Over!!")

        builder.setPositiveButton("Play Again") { dialog, which ->
            val ballIntent = Intent(this, BallActivity::class.java)
            ballIntent.putExtra("sensitivity", "${sensitivity}")
            startActivity(ballIntent)
        }
        builder.setNegativeButton("Back") { dialog, which ->
            val ballInfoIntent = Intent(this,BallInfoActivity::class.java)
            startActivity(ballInfoIntent)
        }
        val timeString = String.format("%02d:%02d", minutes, seconds)
        Log.d("elapsedTime", elapsedTime.toString())
        builder.setMessage("Sensitivity: ${sensitivity} \n Time : ${timeString} \n Score : ${elapsedTime} \n Points: ${points}")
        val dialog = builder.create()
        dialog.show()

        //stop sensor
        sensorManager.unregisterListener(this)
    }
    private fun setUpTimer(){
        val timerTv = findViewById<TextView>(R.id.timerTv)
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                elapsedTime += 1000
                runOnUiThread {
                    val minutes = (elapsedTime / 1000) / 60
                    val seconds = (elapsedTime / 1000) % 60
                    val timeString = String.format("%02d:%02d", minutes, seconds)
                    timerTv.text = timeString
                }
            }
        },0,1000)
    }
    override fun onDestroy() {
        sensorManager.unregisterListener(this)
        super.onDestroy()
    }
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun exitBtn(view: View) {
        val ballInfoIntent = Intent(this, BallInfoActivity::class.java)
        startActivity(ballInfoIntent)
    }

}