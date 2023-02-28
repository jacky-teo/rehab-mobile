package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.*

class RehappMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rehapp_main)
        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val dateFormat = SimpleDateFormat("EEEE, dd MMM")
        val formattedDate = dateFormat.format(currentDate)
        val dateTV = findViewById<TextView>(R.id.dateText)
        dateTV.text = formattedDate
    }

    // Produces the Bottom Sheet containing User Settings
    fun viewAccountBtnClick(view: View) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomView)
        bottomSheetDialog.show()
        bottomView.findViewById<Button>(R.id.btn_close).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    // Logout button present in the Bottom Sheet
    fun logoutBtnClick(view: View) {
        val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}