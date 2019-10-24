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
        val treatmentEventDate: TextView = itemView.findViewById(R.id.eventDate)
        val treatmentPinkEye: TextView = itemView.findViewById(R.id.pinkEye)
        val treatmentEyeSide: TextView = itemView.findViewById(R.id.eyeSide)
        val treatmentRespiratory: TextView = itemView.findViewById(R.id.respiratory)
        val treatmentScours: TextView = itemView.findViewById(R.id.scours)
        val treatmentFoot: TextView = itemView.findViewById(R.id.foot)
        val treatmentFootPosition: TextView = itemView.findViewById(R.id.footPosition)
        val treatmentMastitis: TextView = itemView.findViewById(R.id.mastitis)
        val treatmentOther: TextView = itemView.findViewById(R.id.other)
        val treatmentDetails: TextView = itemView.findViewById(R.id.details)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TreatmentViewHolder {
        val itemView = inflater.inflate(R.layout.treatment_row, parent, false)
        return TreatmentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder, position: Int) {
        val current = treatmentList[position]
        holder.treatmentEventDate.text = current.EventDate
        holder.treatmentPinkEye.text = current.PinkEye
        holder.treatmentEyeSide.text = current.EyeSide
        holder.treatmentRespiratory.text = current.Respiratory
        holder.treatmentScours.text = current.Scours
        holder.treatmentFoot.text = current.Foot
        holder.treatmentFootPosition.text = current.FootPosition
        holder.treatmentMastitis.text = current.Mastitis
        holder.treatmentOther.text = current.Other
        holder.treatmentDetails.text = current.Details
    }

    internal fun setTreatmentList(treatmentList: List<Treatment>) {
        this.treatmentList = treatmentList
        notifyDataSetChanged()
    }

    override fun getItemCount() = treatmentList.size
}