package com.example.rehab_mobile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class AwardsActivity : AppCompatActivity() {
    private var requestCamera: ActivityResultLauncher<String>?=null
    private var username: String? = ""
    private var availPoints: Int = 0

    // voucher value and points
    val voucherPoints = mapOf("v5" to 600, "v10" to 1000, "v15" to 1400)
    var vpoints = 0

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
        replaceFragment(AvailableAwards())

        dbHelper = DatabaseHelper(this)

        // get username
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")

        // TO BE REMOVED !! - Updating user points for testing purposes
//        dbHelper.updateUserPoint(username!!, 1400)
        // TO BE REMOVED !! - End

        // display points
        getPoints()

        //Camera permission
        requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission(), ){
            if(it){
                val intent = Intent(this, QrActivity::class.java)
                intent.putExtra("availpoints", availPoints.toString())
                intent.putExtra("vPoints", vpoints.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this, "Permission not Granted", Toast.LENGTH_SHORT).show()
            }
        }

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
        replaceFragment(AvailableAwards())
        updateTabStyle(view)
    }

    // show vouchers that have already been redeemed
    fun showRedeemed(view: View) {
        replaceFragment(redeemedAwards())
        updateTabStyle(view)

    }

    // get user points
    private fun getPoints() {
        if (dbHelper.searchUserPoint(username!!).isNotEmpty()){
            availPoints = dbHelper.searchUserPoint(username!!)[0].points
        }
        // display points on awards page
        val pointsView = findViewById<TextView>(R.id.availablePoints)
        pointsView.text = availPoints.toString()
    }

    // new redemption of voucher
    fun requestVoucherRedemption(view: View) {
        // get id of clicked voucher
        val viewTag = view.getTag()

        // check if user sufficient points for voucher redemption
        if (availPoints >= voucherPoints[viewTag]!!) {
            vpoints = voucherPoints[viewTag.toString()]!!.toInt()
            requestCamera?.launch(android.Manifest.permission.CAMERA)
        } else {
            val pointDiff = voucherPoints[viewTag]!! - availPoints

            // show insufficient points message
            val toast = Toast.makeText(this, "Need " + pointDiff + " more points! :D", Toast.LENGTH_SHORT)
            toast.show()
        }


    }

    fun backToMainButton(view: View) {
        val homeIntent = Intent(this, HomeActivity::class.java)
        startActivity(homeIntent)
    }

}