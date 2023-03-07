package com.example.rehab_mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rehab_mobile.databinding.ActivityAwardsBinding
import com.example.rehab_mobile.databinding.ActivityHomeBinding

class AwardsActivity : AppCompatActivity() {

    //initiate binding variable
    private lateinit var binding : ActivityAwardsBinding

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_container, fragment)
        fragmentTransaction.commit()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_awards)

        // default - show vouchers available for redemption
        replaceFragment(availableAwards())
    }

    // return to intent origin
    fun returnToPrev(view: View) {
        val intent = Intent()
        setResult(RESULT_OK)
        finish()
    }

    // show vouchers available for redemption
    fun showAvailable(view: View) {
        replaceFragment(availableAwards())
    }

    // show vouchers that have already been redeemed
    fun showRedeemed(view: View) {
        replaceFragment(redeemedAwards())
    }


}