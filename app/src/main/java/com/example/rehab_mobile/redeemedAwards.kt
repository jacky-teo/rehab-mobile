package com.example.rehab_mobile

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class redeemedAwards : Fragment() {
    // db helper
    private lateinit var dbHelper: DatabaseHelper
    private var username: String? = ""

    // recycler view variables
    private lateinit var myAdapter: RecordsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var recordsArrayList: ArrayList<RewardModelRecord>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dbHelper = DatabaseHelper(requireActivity())

        // get username
        val sharedPreference = requireActivity()!!.getSharedPreferences("rehapp_login", Context.MODE_PRIVATE)
        username = sharedPreference.getString("username","")


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // load redemption history
        loadRedeemedRewards()

        val layoutManager = LinearLayoutManager(context)
        recyclerView = view.findViewById(R.id.historyList)
        recyclerView.layoutManager = layoutManager
        recyclerView.setHasFixedSize(true)
        myAdapter = RecordsAdapter(recordsArrayList)
        recyclerView.adapter = myAdapter

//        myAdapter.notifyDataSetChanged()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_redeemed_awards, container, false)
    }

    // load history of voucher redemption
    private fun loadRedeemedRewards(){
        recordsArrayList = dbHelper.searchUserReward(username!!)
        val msg = requireActivity()!!.findViewById<TextView>(R.id.redeemedMsg)

        // hide message if redemption records not empty
        if (recordsArrayList.size > 0){
            recordsArrayList.reverse()
            msg.visibility = View.GONE

        } else {
            msg.text = "No Vouchers Redeemed"
        }

    }

}