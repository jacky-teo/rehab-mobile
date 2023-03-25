package com.example.rehab_mobile


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.text.SimpleDateFormat
import java.util.*

class Home : Fragment() {
    private lateinit var dbHelper: DatabaseHelper
    private var username: String? = ""
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
        dbHelper = DatabaseHelper(requireActivity())


        val calendar = Calendar.getInstance()
        val currentDate = calendar.time
        val dateFormat = SimpleDateFormat("EEEE, dd MMM")
        val formattedDate = dateFormat.format(currentDate)
        val dateTV = act!!.findViewById<TextView>(R.id.dateText)
        dateTV.text = formattedDate

        // Set Data for Daily activity
        // Get username
        val sharedPreference =requireActivity().getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")
        // Check if current day's activity is empty
        val todayDateFormat  = SimpleDateFormat("yyyy-MM-dd")
        val todayDateFormatted = todayDateFormat.format(currentDate)
        if(dbHelper.searchStepActivityRecords(username!!,todayDateFormatted).isEmpty()){
            // Create entry for today along with the points
            dbHelper.insertStepActivity(username!!,todayDateFormatted,0)
        }

        if(dbHelper.searchBallActivityRecords(username!!,todayDateFormatted).isEmpty()){
            // Create entry for today along with the points
            dbHelper.insertBallActivity(username!!,todayDateFormatted,0)
        }
        //Set Data for summary Segment
        val summaryBallTv = act!!.findViewById<TextView>(R.id.ballSummary)
        val stepSummaryTv = act!!.findViewById<TextView>(R.id.stepSummary)
        var allSteps = 0
        var allBalls = 0
        val stepActivity = dbHelper.searchStepUserActivityRecords(username!!)
        val ballActivity = dbHelper.searchBallUserActivityRecords(username!!)
        for(dat in stepActivity){
            allSteps += dat.stepitup

        }
        for (dat in ballActivity){
            allBalls += dat.ballbalance
        }
        summaryBallTv.text = allBalls.toString()
        stepSummaryTv.text = allSteps.toString()

        // Set the data to be shown on home screen for daily activities
        val activityStepData = dbHelper.searchStepActivityRecords(username!!,todayDateFormatted)
        val activityBallData = dbHelper.searchBallActivityRecords(username!!,todayDateFormatted)
        val dailyBallTv = act!!.findViewById<TextView>(R.id.todayBallGame)
        val dailyStepTv = act!!.findViewById<TextView>(R.id.todayStepGame)
        val currentStepData = activityStepData[0]
        val currentBallData = activityBallData[0]
        dailyBallTv.text = currentBallData.ballbalance.toString()
        dailyStepTv.text = currentStepData.stepitup.toString()

        val stepActivityLayout = act.findViewById<LinearLayout>(R.id.stepInfoActivityLayout) as LinearLayout
        stepActivityLayout.setOnClickListener {
            val intent = Intent(act, StepInfoActivity::class.java)
            act.startActivity(intent)
            act.finish()
        }


    }




}