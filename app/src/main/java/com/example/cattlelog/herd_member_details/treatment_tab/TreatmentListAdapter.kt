package com.example.cattlelog.herd_member_details.treatment_tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.R
import com.example.cattlelog.model.entities.Treatment

class TreatmentListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<TreatmentListAdapter.TreatmentViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var treatmentList = emptyList<Treatment>() // Cached copy of treatments

    inner class TreatmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val treatmentItemView0: TextView = itemView.findViewById(R.id.eventDate)
        val treatmentItemView1: TextView = itemView.findViewById(R.id.pinkEye)
        val treatmentItemView2: TextView = itemView.findViewById(R.id.eyeSide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        val itemView = inflater.inflate(R.layout.treatment_row, parent, false)
        return TreatmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val current = treatmentList[position]
        holder.treatmentItemView0.text = current.EventDate
        holder.treatmentItemView1.text = current.PinkEye
        holder.treatmentItemView2.text = current.EyeSide
    }

    internal fun setTreatmentList(treatmentList: List<Treatment>) {
        this.treatmentList = treatmentList
        notifyDataSetChanged()
    }

    override fun getItemCount() = treatmentList.size
}