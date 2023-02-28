package com.example.rehab_mobile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterRecord(): RecyclerView.Adapter<AdapterRecord.HolderRecord>() {
    private var context:Context?=null
    private var recordList: ArrayList<ModelRecord>?=null

    constructor(context: Context, recordList: ArrayList<ModelRecord>?): this(){
        this.context = context
        this.recordList = recordList
    }
    class HolderRecord(itemView:View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.username)
        var password: TextView = itemView.findViewById(R.id.password)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderRecord {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: HolderRecord, position: Int) {
        TODO("Not yet implemented")
    }
}