package com.example.rehab_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rehab_mobile.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    //initiate binding variable
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // when page first launched, display home fragment
        replaceFragment(Home())

        // declare bottom navbar listener
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.summary -> replaceFragment(Home())
                R.id.awards -> replaceFragment(Awards())

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()

    }


}