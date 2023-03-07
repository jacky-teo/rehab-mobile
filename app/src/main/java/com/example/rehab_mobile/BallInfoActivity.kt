package com.example.rehab_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.slider.Slider

class BallInfoActivity : AppCompatActivity() {
    lateinit var sensitivityVal:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ball_info)

        val slider = findViewById<Slider>(R.id.slider)
        val sensitivityVal = findViewById<TextView>(R.id.sensitivityVal)
        slider.addOnChangeListener { slider, value, fromUser ->
            sensitivityVal.text = value.toString()
        }
    }

    fun startGameBtn(view: View) {
//        val myIntent = Intent(this, BalancingActivity::class.java)
//        myIntent.putExtra("sensitivity",sensitivityVal.text)
//        startActivity(myIntent)
    }

}