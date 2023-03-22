package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rehab_mobile.databinding.ActivityHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class HomeActivity : AppCompatActivity() {

    //initiate binding variable
    private lateinit var binding : ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val formatter = SimpleDateFormat("dd-MM-yyyy")
//        val date = Date()
//        val currentDate = formatter.format(date).toString()
//        val sharedPreferences = getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
//        val username = sharedPreferences.getString("username","")
//        val userActivity = dbHelper.searchActivityRecords(username!!, currentDate)
//        Log.d("User Activity", userActivity.toString())
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // when page first launched, display home fragment
        replaceFragment(Home())

        // declare bottom navbar listener
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.summary -> replaceFragment(Home())
                R.id.user -> replaceFragment(User())

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


    // Produces the Bottom Sheet containing User Settings
//    fun viewAccountBtnClick(view: View) {
//        val bottomSheetDialog = BottomSheetDialog(this)
//        val bottomView = layoutInflater.inflate(R.layout.bottom_sheet, null)
//        bottomSheetDialog.setContentView(bottomView)
//        bottomSheetDialog.show()
//        bottomView.findViewById<Button>(R.id.btn_close).setOnClickListener {
//            bottomSheetDialog.dismiss()
//        }
//    }

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

    fun stepActivityBtnClick(view: View){
        val intent = Intent(this,StepActivity::class.java )
        startActivity(intent)
        finish()
    }

    // Go to ball game info activity
    fun viewBallGameInfo(view: View) {
        val intent = Intent(this,BallInfoActivity::class.java )
        startActivity(intent)
        finish()
    }


    // Go to steps tracker info activity
    fun viewStepsInfo(view: View) {
        val intent = Intent(this,StepInfoActivity::class.java )
        startActivity(intent)
        finish()
    }

    // Go to awards activity
    fun viewAwards(view: View) {
        val intent = Intent(this, AwardsActivity::class.java)
        val requestCode = 123
        startActivityForResult(intent, requestCode)

    }
}