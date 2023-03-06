package com.example.rehab_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class AwardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awards)
    }

    fun returnToPrev(view: View) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("nav_state", "user")
        startActivity(intent)
        finish()
    }
}