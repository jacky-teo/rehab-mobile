package com.example.rehab_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.google.android.material.slider.Slider

class BallInfoActivity : AppCompatActivity() {
    lateinit var sensitivityVal: TextView
    private var sensitivity = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ball_info)

        val slider = findViewById<Slider>(R.id.slider)
        val sensitivityVal = findViewById<TextView>(R.id.sensitivityVal)
        slider.addOnChangeListener { slider, value, fromUser ->
            sensitivityVal.text = value.toString()
            sensitivity = value.toInt()
        }
    }

    fun startGameBtn(view: View) {
        val ballIntent = Intent(this, BallActivity::class.java)
        ballIntent.putExtra("sensitivity", "${sensitivity}")
        startActivity(ballIntent)
    }
    fun backBtn(view: View) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }
}