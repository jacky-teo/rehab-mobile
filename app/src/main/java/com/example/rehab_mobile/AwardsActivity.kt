package com.example.rehab_mobile

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.rehab_mobile.databinding.QrPopUpBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.google.zxing.multi.MultipleBarcodeReader
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat
import java.util.*

class AwardsActivity : AppCompatActivity() {

    private var username: String? = ""
    private var availPoints: Int = 0
    lateinit var barcodeDetector: BarcodeDetector
    lateinit var cameraSource: CameraSource

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
        replaceFragment(AvailableAwards())

        dbHelper = DatabaseHelper(this)

        // get username
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")

        // TO BE REMOVED !! - Updating user points for testing purposes
        dbHelper.updateUserPoint(username!!, 1400)
        // TO BE REMOVED !! - End

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


    // deduct points for redemption and add redemption record
    private fun redeemNew(vId: String) {
        // get and format current time and date
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy   HH:mm")
        val todayDateFormatted = dateFormat.format(currentDateTime)

        // insert new record of redemption into db
        dbHelper.insertRedemption(username!!, vId.substring(1).toInt(), todayDateFormatted)

        // update user points after voucher redemption
        val updatedPoints = availPoints - voucherPoints[vId]!!.toInt()
        dbHelper.updateUserPoint(username!!, updatedPoints)

        // reload points shown
        getPoints()
        Log.d("userPoints", dbHelper.searchUserPoint(username!!)[0].points.toString())
    }

    // new redemption of voucher
    fun requestVoucherRedemption(view: View) {
        // get id of clicked voucher
        val viewTag = view.getTag()

        // check if user sufficient points for voucher redemption
        if (availPoints >= voucherPoints[viewTag]!!) {
            //Camera access
            requestCameraPermission()

            // show pop up with generate qr code
            val popUpBinding = layoutInflater.inflate(R.layout.qr_pop_up, null)

            val popDialog = Dialog(this)
            popDialog.setContentView(popUpBinding)

            popDialog.setCancelable(true)
            popDialog.window?.setBackgroundDrawable(ColorDrawable(Color.argb(29,0, 0, 0)))
//
//            // set onclicklistener to redeem button
//            val redeemBtn = popDialog.findViewById<Button>(R.id.redeemedBtn)
//            redeemBtn.setOnClickListener() {
//                // deduct points for redemption
//                redeemNew(viewTag.toString())
//
//                // dismiss dialog after voucher redeemed
//                popDialog.dismiss()
//
//                // show redemption complete message
//                val toast = Toast.makeText(this, "Voucher redeemed!", Toast.LENGTH_SHORT)
//                toast.show()
//            }
//
            popDialog.show()
            // set close dialog button
            val closeButton = popUpBinding.findViewById<ImageView>(R.id.closeBtn)
            closeButton.setOnClickListener() {
                popDialog.dismiss()
            }

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

    //Request Camera access functions
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // Permission already granted. Start the scanner
            startScanner()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                startScanner()
            } else {
                // Permission denied
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT ).show()
            }
        }
    }
//    private fun startScanner() {
//        barcodeDetector = BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.ALL_FORMATS).build()
//        cameraSource = CameraSource.Builder(this, barcodeDetector)
//            .setRequestedPreviewSize(1920,1080)
//            .setAutoFocusEnabled(true)
//            .build()
//    }
    // Start the scanner
    private fun startScanner() {
        IntentIntegrator(this).initiateScan()
    }

    // Handle the result of the scan
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                // The scan was cancelled
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                // The scan was successful
                Toast.makeText(this, "Scan result: ${result.contents}", Toast.LENGTH_SHORT).show()
            }
        }
    }

}