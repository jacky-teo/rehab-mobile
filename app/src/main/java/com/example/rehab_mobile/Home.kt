package com.example.rehab_mobile

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikhaellopez.circularimageview.CircularImageView
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATION")
class Home : Fragment(), View.OnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val act = activity

        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val dateFormat = SimpleDateFormat("EEEE, dd MMM")
        val formattedDate = dateFormat.format(currentDate)
        val dateTV = act!!.findViewById<TextView>(R.id.dateText)
        dateTV.text = formattedDate

//        // check if activity is null
//        if (act != null) {
//            // set event listener for account icon
            val accButton = act.findViewById<CircularImageView>(R.id.viewAccountBtn)
            accButton.setOnClickListener(this)
//        }


    }

    // OnClick function for home fragment - switch case function calls
    override fun onClick(v: View?) {
        val id = view?.id

        if (id == R.id.viewAccountBtn) {
            Log.i("btn click", "clicked!")
            viewAccountBtnClick(view)
        }
    }

    // Produces the Bottom Sheet containing User Settings
    private fun viewAccountBtnClick(view: View?) {
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        val bottomView = layoutInflater.inflate(R.layout.bottom_sheet, null)
        bottomSheetDialog.setContentView(bottomView)
        bottomSheetDialog.show()
        bottomView.findViewById<Button>(R.id.btn_close).setOnClickListener {
            bottomSheetDialog.dismiss()
        }
    }

    // Logout button present in the Bottom Sheet
    fun logoutBtnClick(view: View) {
        val sharedPreferences = requireActivity().getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(activity, MainActivity::class.java)
        startActivity(intent)
    }

}