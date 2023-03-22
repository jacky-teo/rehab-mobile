package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.rehab_mobile.databinding.ActivityAwardsBinding
import com.example.rehab_mobile.databinding.ActivityHomeBinding
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AwardsActivity : AppCompatActivity() {

    private var username: String? = ""

    // voucher value and points
    val voucherPoints = mapOf("v5" to 600, "v10" to 1000, "v15" to 1400)

    // db helper
    private lateinit var dbHelper: DatabaseHelper

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()

        // set available as default active button tab
        updateTabStyle(findViewById<Button>(R.id.availableBtn))

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awards)

        // default - show vouchers available for redemption
        replaceFragment(availableAwards())

        dbHelper = DatabaseHelper(this)

        // get username
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")

        // display points
        getPoints()
    }

    // update button styling
    private fun updateTabStyle(view: View) {
        val availBtn = findViewById<Button>(R.id.availableBtn)
        val redBtn = findViewById<Button>(R.id.redeemedBtn)

        // set styling of button clicked
        view.setBackgroundResource(R.drawable.active_tab_btns)

        // update styling of other button
        if (view == availBtn) {
            redBtn.setBackgroundResource(R.drawable.inactive_tab_btns)
            redBtn.setTextColor(ContextCompat.getColor(this, R.color.darkGray))

            availBtn.setTextColor(ContextCompat.getColor(this, R.color.lightBrown))
        } else {
            availBtn.setBackgroundResource(R.drawable.inactive_tab_btns)
            availBtn.setTextColor(ContextCompat.getColor(this, R.color.darkGray))

            redBtn.setTextColor(ContextCompat.getColor(this, R.color.lightBrown))
        }
    }

    // show vouchers available for redemption
    fun showAvailable(view: View) {
        replaceFragment(availableAwards())
        updateTabStyle(view)
    }

    // show vouchers that have already been redeemed
    fun showRedeemed(view: View) {
        replaceFragment(redeemedAwards())
        updateTabStyle(view)

    }

    // get user points
    private fun getPoints() {
        val availPoints = dbHelper.searchUserPoint(username!!)[0].points

        // display points on awards page
        val pointsView = findViewById<TextView>(R.id.availablePoints)
        pointsView.text = availPoints.toString()
    }


    // deduct points for redemption and add redemption record
    private fun redeemNew(voucherValue: String) {
        // get and format current time and date
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE.dd.LLLL.yyyy HH:mm:ss aaa z")
        val todayDateFormatted = dateFormat.format(currentDateTime)

        // update user points after voucher redemption
        dbHelper.updateUserPoint(username!!, voucherPoints[voucherValue]!!.toInt())

        // insert new record of redemption into db
        dbHelper.insertRedemption(username!!, voucherValue.substring(1).toInt(), todayDateFormatted)
    }

    // new redemption of voucher
    fun requestVoucherRedemption(view: View) {
        // get id of clicked voucher
        val viewTag = view.getTag()
        val voucherValue = viewTag.toString().substring(1)
        Log.i("Voucher", voucherValue)

        // check if user sufficient points for voucher redemption

        // generate QR code
        // https://youtu.be/n8HdrLYL9DA
        // https://ihilalahmadd.medium.com/how-to-generate-qr-code-in-android-5a2a7edf11c

    }

}