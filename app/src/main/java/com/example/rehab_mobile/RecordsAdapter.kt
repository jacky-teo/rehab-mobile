package com.example.rehab_mobile

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecordsAdapter (private val arrayList: ArrayList<RewardModelRecord>) : RecyclerView.Adapter<RecordsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordsAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.redeemed_voucher, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecordsAdapter.MyViewHolder, position: Int) {
        val item = arrayList[position]
        holder.valueTv.text = "$" + item.vouchervalue.toString()
        holder.timeTv.text = item.dateofredemption
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val valueTv : TextView = itemView.findViewById(R.id.value)
        val timeTv : TextView = itemView.findViewById(R.id.time)
    }
}