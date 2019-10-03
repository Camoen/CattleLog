package com.example.cattlelog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.R
import com.example.cattlelog.model.entities.Cattle

class CattleListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<CattleListAdapter.CattleViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cattleList = emptyList<Cattle>() // Cached copy of cattle

    inner class CattleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cattleItemView0: TextView = itemView.findViewById(R.id.herdTagNumber)
        val cattleItemView1: TextView = itemView.findViewById(R.id.herdBirthDate)
        val cattleItemView2: TextView = itemView.findViewById(R.id.herdBarnName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CattleViewHolder {
        val itemView = inflater.inflate(R.layout.list_row, parent, false)
        return CattleViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CattleViewHolder, position: Int) {
        val current = cattleList[position]
        holder.cattleItemView0.text = current.TagNumber.toString()
        holder.cattleItemView1.text = current.BirthDate
        holder.cattleItemView2.text = current.BarnName
    }

    internal fun setCattleList(cattleList: List<Cattle>) {
        this.cattleList = cattleList
        notifyDataSetChanged()
    }

    override fun getItemCount() = cattleList.size
}