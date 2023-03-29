package com.example.rehab_mobile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.wifi.aware.AwareResources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.rehab_mobile.databinding.ActivityQrBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class QrActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQrBinding
    //For QR scanner
    private lateinit var barcodeDetector: BarcodeDetector
    private lateinit var cameraSource: CameraSource
    var intentData = " "
    //points
    private var username: String? = ""
    private var vpoints = 0
    private var availpoints = 0
    private var count = 0
    // db helper
    private lateinit var dbHelper: DatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // get username
        val sharedPreference = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")

        vpoints = intent.getStringExtra("vPoints")!!.toInt()
        availpoints = intent.getStringExtra("availpoints")!!.toInt()
        dbHelper = DatabaseHelper(this)
    }
    private fun iniBc(){
        barcodeDetector = BarcodeDetector.Builder(this)
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()
        cameraSource = CameraSource.Builder(this,barcodeDetector)
            .setRequestedPreviewSize(1920,1080)
            .setAutoFocusEnabled(true)
            .build()
        binding.surfaceView!!.holder.addCallback(object : SurfaceHolder.Callback{
            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("MissingPermission")
            override fun surfaceCreated(p0: SurfaceHolder) {
                try{
                    cameraSource.start(binding.surfaceView!!.holder)
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(p0: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
//                Toast.makeText(applicationContext, "barcode scanner has been stopped", Toast.LENGTH_SHORT).show()
            }
            override fun receiveDetections(p0: Detector.Detections<Barcode>) {
                val barcodes = p0.detectedItems
                if(barcodes.size()!=0){
                    binding.txtBarcodeValue!!.post{
//                        binding.btnAction!!.text = "Search Item"
                        intentData = barcodes.valueAt(0).displayValue
//                        binding.txtBarcodeValue.setText(intentData)
                        if (intentData == "Fairprice" && count == 0){
                            Log.d("redeemlog", "fairprice")
                            redeemNew()
                            val awardsIntent = Intent(this@QrActivity, AwardsActivity::class.java)
                            startActivity(awardsIntent)
                            count++
                        }
                    }
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()
        cameraSource!!.release()
    }

    override fun onResume() {
        super.onResume()
        iniBc()
    }

    fun backBtn(view: View) {
        val awardsIntent = Intent(this@QrActivity, AwardsActivity::class.java)
        startActivity(awardsIntent)
    }

    // deduct points for redemption and add redemption record
    private fun redeemNew() {
        // get and format current time and date
        val currentDateTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, dd LLLL yyyy   HH:mm")
        val todayDateFormatted = dateFormat.format(currentDateTime)

        // insert new record of redemption into db
        dbHelper.insertRedemption(username!!, vpoints, todayDateFormatted)

        // update user points after voucher redemption
        val updatedPoints = availpoints - vpoints
        dbHelper.updateUserPoint(username!!, updatedPoints)

        // reload points shown
        Toast.makeText(this, "Voucher successfully redeemed!", Toast.LENGTH_SHORT).show()
    }
}