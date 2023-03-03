package com.example.rehab_mobile


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class Home : Fragment() {

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

        val stepActivityLayout = act.findViewById<LinearLayout>(R.id.stepActivityLayout) as LinearLayout
        stepActivityLayout.setOnClickListener {
            val intent = Intent(act, StepActivity::class.java)
            act.startActivity(intent)
            act.finish()
        }


    }




}