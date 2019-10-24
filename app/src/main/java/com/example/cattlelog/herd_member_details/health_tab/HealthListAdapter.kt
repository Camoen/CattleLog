package com.example.cattlelog.herd_member_details.health_tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.R
import com.example.cattlelog.model.entities.Health

class HealthListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<HealthListAdapter.HealthViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var healthList = emptyList<Health>() // Cached copy of health events

    inner class HealthViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val healthEventView: TextView = itemView.findViewById(R.id.healthEvent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthViewHolder {
        val itemView = inflater.inflate(R.layout.health_row, parent, false)
        return HealthViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HealthViewHolder, position: Int) {
        val current = healthList[position]
        holder.healthEventView.text = current.HealthEvent
    }

    internal fun setHealthList(healthList: List<Health>) {
        this.healthList = healthList
        notifyDataSetChanged()
    }

    override fun getItemCount() = healthList.size
}