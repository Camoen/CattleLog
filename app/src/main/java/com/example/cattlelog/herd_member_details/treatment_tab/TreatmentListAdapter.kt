package com.example.cattlelog.herd_member_details.treatment_tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
        val treatmentPinkEyeLine: LinearLayout = itemView.findViewById(R.id.pinkEyeLine)
        val treatmentPinkEye: TextView = itemView.findViewById(R.id.pinkEye)
        val treatmentPinkEyeLabel: TextView = itemView.findViewById(R.id.pinkEyeLabel)
        val treatmentEyeSide: TextView = itemView.findViewById(R.id.eyeSide)
        val treatmentEyeSideLabel: TextView = itemView.findViewById(R.id.eyeSideLabel)
        val treatmentRespiratoryLine: LinearLayout = itemView.findViewById(R.id.respiratoryLine)
        val treatmentRespiratory: TextView = itemView.findViewById(R.id.respiratory)
        val treatmentScoursLine: LinearLayout = itemView.findViewById(R.id.scoursLine)
        val treatmentScours: TextView = itemView.findViewById(R.id.scours)
        val treatmentFootLine: LinearLayout = itemView.findViewById(R.id.footLine)
        val treatmentFoot: TextView = itemView.findViewById(R.id.foot)
        val treatmentFootLabel: TextView = itemView.findViewById(R.id.footLabel)
        val treatmentFootPosition: TextView = itemView.findViewById(R.id.footPosition)
        val treatmentFootPositionLabel: TextView = itemView.findViewById(R.id.footPositionLabel)
        val treatmentMastitisLine: LinearLayout = itemView.findViewById(R.id.mastitisLine)
        val treatmentMastitis: TextView = itemView.findViewById(R.id.mastitis)
        val treatmentOtherLine: LinearLayout = itemView.findViewById(R.id.otherLine)
        val treatmentOther: TextView = itemView.findViewById(R.id.other)
        val treatmentDetails: TextView = itemView.findViewById(R.id.details)
        val treatmentDetailsLabel: TextView = itemView.findViewById(R.id.detailsLabel)
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

        // Set visibility for individual eye fields and entire eye row
        if (current.PinkEye.isNullOrBlank()) setVisibility(holder.treatmentPinkEyeLabel, holder.treatmentPinkEye)
        if (current.EyeSide.isNullOrBlank()) setVisibility(holder.treatmentEyeSideLabel, holder.treatmentEyeSide)
        if (current.PinkEye.isNullOrBlank() && current.EyeSide.isNullOrBlank()) holder.treatmentPinkEyeLine.visibility = View.GONE

        // Set visibility for individual foot fields and entire foot row
        if (current.Foot.isNullOrBlank()) setVisibility(holder.treatmentFootLabel, holder.treatmentFoot)
        if (current.FootPosition.isNullOrBlank()) setVisibility(holder.treatmentFootPositionLabel, holder.treatmentFootPosition)
        if (current.Foot.isNullOrBlank() && current.FootPosition.isNullOrBlank()) holder.treatmentFootLine.visibility = View.GONE

        // Set visibility for entire row of unpaired treatment attributes
        if (current.Respiratory.isNullOrBlank()) holder.treatmentRespiratoryLine.visibility = View.GONE
        if (current.Scours.isNullOrBlank()) holder.treatmentScoursLine.visibility = View.GONE
        if (current.Mastitis.isNullOrBlank()) holder.treatmentMastitisLine.visibility = View.GONE
        if (current.Other.isNullOrBlank()) holder.treatmentOtherLine.visibility = View.GONE
        if (current.Details.isNullOrBlank()) setVisibility(holder.treatmentDetailsLabel, holder.treatmentDetails)

    }

    private fun setVisibility(label: TextView, value: TextView){
        label.visibility = View.GONE
        value.visibility = View.GONE
    }

    internal fun setTreatmentList(treatmentList: List<Treatment>) {
        this.treatmentList = treatmentList
        notifyDataSetChanged()
    }

    override fun getItemCount() = treatmentList.size
}