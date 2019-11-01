package com.example.cattlelog.herd_member_details.overview_tab

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.R
import com.example.cattlelog.model.entities.Cattle
import com.example.cattlelog.model.entities.UserFields


class OverviewListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<OverviewListAdapter.OverviewViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var overviewList = emptyList<Cattle>()  // Cached copy of individual animal details
    private var userFieldsList = emptyList<UserFields>() // Cached copy of userFields

    inner class OverviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val animalBirthDate: TextView = itemView.findViewById(R.id.birthDate)
        val animalDaysSinceBredHeat: TextView = itemView.findViewById(R.id.daysSinceBredHeat)
        val animalDaysTilDue: TextView = itemView.findViewById(R.id.daysTilDue)
        val animalGroupNumber: TextView = itemView.findViewById(R.id.groupNumber)
        val animalTempGroupNumber: TextView = itemView.findViewById(R.id.tempGroupNumber)
        val animalReproCode: TextView = itemView.findViewById(R.id.reproCode)
        val animalTimesBred: TextView = itemView.findViewById(R.id.timesBred)
        val animalUsrDef1: TextView = itemView.findViewById(R.id.userDef1)
        val animalUsrDef2: TextView = itemView.findViewById(R.id.userDef2)
        val animalUsrDef3: TextView = itemView.findViewById(R.id.userDef3)
        val animalUsrDef4: TextView = itemView.findViewById(R.id.userDef4)
        val animalUsrDef5: TextView = itemView.findViewById(R.id.userDef5)
        val animalUsrDef6: TextView = itemView.findViewById(R.id.userDef6)
        val animalUsrDef7: TextView = itemView.findViewById(R.id.userDef7)
        val animalUsrDef72: TextView = itemView.findViewById(R.id.userDef7_2)
        val animalUsrDef8: TextView = itemView.findViewById(R.id.userDef8)
        val animalUsrDef9: TextView = itemView.findViewById(R.id.userDef9)
        val animalUsrDef10: TextView = itemView.findViewById(R.id.userDef10)
        val animalDaysTilNextHeat: TextView = itemView.findViewById(R.id.daysTilNextHeat)
        val animalBarnName: TextView = itemView.findViewById(R.id.barnName)
        val animalDHIID: TextView = itemView.findViewById(R.id.dhiID)
        val animalDamIndex: TextView = itemView.findViewById(R.id.damIndex)
        val animalDamIndex2: TextView = itemView.findViewById(R.id.damIndex2)
        val animalDamName: TextView = itemView.findViewById(R.id.damName)
        val animalSireNameCode: TextView = itemView.findViewById(R.id.sireNameCode)
        val animalSireNameCode2: TextView = itemView.findViewById(R.id.sireNameCode2)
        val animalTimesBredDate: TextView = itemView.findViewById(R.id.timesBredDate)
        val animalDateDue: TextView = itemView.findViewById(R.id.dateDue)
        val animalServiceSireNameCode: TextView = itemView.findViewById(R.id.serviceSireNameCode)
        val animalNextExpHeat: TextView = itemView.findViewById(R.id.nextExpHeat)
        val animalDonorDamID: TextView = itemView.findViewById(R.id.donorDamID)
        val animalFarmID: TextView = itemView.findViewById(R.id.farmID)
        val animalDamDHIID: TextView = itemView.findViewById(R.id.damDHI_ID)
        val animalPrevBredHeat1: TextView = itemView.findViewById(R.id.prevBredHeat1)
        val animalPrevBredHeat2: TextView = itemView.findViewById(R.id.prevBredHeat2)
        val animalPrevBredHeat3: TextView = itemView.findViewById(R.id.prevBredHeat3)
        val animalWeightBirth: TextView = itemView.findViewById(R.id.weightBirth)
        val animalWeightWean: TextView = itemView.findViewById(R.id.weightWean)
        val animalWeightBred: TextView = itemView.findViewById(R.id.weightBred)
        val animalWeightPuberty: TextView = itemView.findViewById(R.id.weightPuberty)
        val animalWeightCalving: TextView = itemView.findViewById(R.id.weightCalving)
        val animalDaysInCurGroup: TextView = itemView.findViewById(R.id.daysInCurGroup)
        val animalDateLeft: TextView = itemView.findViewById(R.id.dateLeft)
        val animalReason: TextView = itemView.findViewById(R.id.reason)
        val animalDateLeftLabel: TextView = itemView.findViewById(R.id.labelDateLeft)
        val animalReasonLabel: TextView = itemView.findViewById(R.id.labelReason)


        val userDefinedLabel1: TextView = itemView.findViewById(R.id.userDefLabel1)
        val userDefinedLabel2: TextView = itemView.findViewById(R.id.userDefLabel2)
        val userDefinedLabel3: TextView = itemView.findViewById(R.id.userDefLabel3)
        val userDefinedLabel4: TextView = itemView.findViewById(R.id.userDefLabel4)
        val userDefinedLabel5: TextView = itemView.findViewById(R.id.userDefLabel5)
        val userDefinedLabel6: TextView = itemView.findViewById(R.id.userDefLabel6)
        val userDefinedLabel7: TextView = itemView.findViewById(R.id.userDefLabel7)
        val userDefinedLabel8: TextView = itemView.findViewById(R.id.userDefLabel8)
        val userDefinedLabel9: TextView = itemView.findViewById(R.id.userDefLabel9)
        val userDefinedLabel10: TextView = itemView.findViewById(R.id.userDefLabel10)

        val userDefinedDescription1: TextView = itemView.findViewById(R.id.userDefDescription1)
        val userDefinedDescription2: TextView = itemView.findViewById(R.id.userDefDescription2)
        val userDefinedDescription3: TextView = itemView.findViewById(R.id.userDefDescription3)
        val userDefinedDescription4: TextView = itemView.findViewById(R.id.userDefDescription4)
        val userDefinedDescription5: TextView = itemView.findViewById(R.id.userDefDescription5)
        val userDefinedDescription6: TextView = itemView.findViewById(R.id.userDefDescription6)
        val userDefinedDescription7: TextView = itemView.findViewById(R.id.userDefDescription7)
        val userDefinedDescription8: TextView = itemView.findViewById(R.id.userDefDescription8)
        val userDefinedDescription9: TextView = itemView.findViewById(R.id.userDefDescription9)
        val userDefinedDescription10: TextView = itemView.findViewById(R.id.userDefDescription10)

        val userDefinedLabel1b: TextView = itemView.findViewById(R.id.userDefLabel1b)
        val userDefinedLabel2b: TextView = itemView.findViewById(R.id.userDefLabel2b)
        val userDefinedLabel3b: TextView = itemView.findViewById(R.id.userDefLabel3b)
        val userDefinedLabel4b: TextView = itemView.findViewById(R.id.userDefLabel4b)
        val userDefinedLabel5b: TextView = itemView.findViewById(R.id.userDefLabel5b)
        val userDefinedLabel6b: TextView = itemView.findViewById(R.id.userDefLabel6b)
        val userDefinedLabel7b: TextView = itemView.findViewById(R.id.userDefLabel7b)
        val userDefinedLabel8b: TextView = itemView.findViewById(R.id.userDefLabel8b)
        val userDefinedLabel9b: TextView = itemView.findViewById(R.id.userDefLabel9b)
        val userDefinedLabel10b: TextView = itemView.findViewById(R.id.userDefLabel10b)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OverviewViewHolder {
        val itemView = inflater.inflate(R.layout.overview_row, parent, false)
        return OverviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: OverviewViewHolder, position: Int) {
        val current = overviewList[position]
        holder.animalBirthDate.text = current.BirthDate
        holder.animalDaysSinceBredHeat.text = current.DaysSinceBredHeat.toString()
        holder.animalDaysTilDue.text = current.DaysTilDue.toString()

        holder.animalGroupNumber.text = current.GroupNumber.toString()
        holder.animalTempGroupNumber.text = current.TempGroupNumber.toString()
        holder.animalReproCode.text = current.ReproCode
        holder.animalTimesBred.text = current.TimesBred.toString()
        holder.animalUsrDef1.text = current.UsrDef1
        holder.animalUsrDef2.text = current.UsrDef2
        holder.animalUsrDef3.text = current.UsrDef3
        holder.animalUsrDef4.text = current.UsrDef4
        holder.animalUsrDef5.text = current.UsrDef5
        holder.animalUsrDef6.text = current.UsrDef6
        holder.animalUsrDef7.text = current.UsrDef7
        holder.animalUsrDef72.text = current.UsrDef7
        holder.animalUsrDef8.text = current.UsrDef8
        holder.animalUsrDef9.text = current.UsrDef9
        holder.animalUsrDef10.text = current.UsrDef10
        holder.animalDaysTilNextHeat.text = current.DaysTilNextHeat.toString()
        holder.animalBarnName.text = current.BarnName
        holder.animalDHIID.text = current.DHIID
        holder.animalDamIndex.text = current.DamIndex.toString()
        holder.animalDamIndex2.text = current.DamIndex.toString()
        holder.animalDamName.text = current.DamName
        holder.animalSireNameCode.text = current.SireNameCode
        holder.animalSireNameCode2.text = current.SireNameCode
        holder.animalTimesBredDate.text = current.TimesBredDate
        holder.animalDateDue.text = current.DateDue
        holder.animalServiceSireNameCode.text = current.ServiceSireNameCode
        holder.animalNextExpHeat.text = current.NextExpHeat
        holder.animalDonorDamID.text = current.DonorDamID
        holder.animalFarmID.text = current.FarmID
        holder.animalDamDHIID.text = current.DamDHI_ID
        holder.animalPrevBredHeat1.text = current.PrevBredHeat1
        holder.animalPrevBredHeat2.text = current.PrevBredHeat2
        holder.animalPrevBredHeat3.text = current.PrevBredHeat3
        holder.animalWeightBirth.text = current.WeightBirth.toString()
        holder.animalWeightWean.text = current.WeightWean.toString()
        holder.animalWeightBred.text = current.WeightBred.toString()
        holder.animalWeightPuberty.text = current.WeightPuberty.toString()
        holder.animalWeightCalving.text = current.WeightCalving.toString()
        holder.animalDaysInCurGroup.text = current.DaysInCurGroup.toString()
        holder.animalDateLeft.text = current.DateLeft
        holder.animalReason.text = current.Reason

        holder.userDefinedLabel1.text = userFieldsList[0].FieldTitle
        holder.userDefinedLabel2.text = userFieldsList[1].FieldTitle
        holder.userDefinedLabel3.text = userFieldsList[2].FieldTitle
        holder.userDefinedLabel4.text = userFieldsList[3].FieldTitle
        holder.userDefinedLabel5.text = userFieldsList[4].FieldTitle
        holder.userDefinedLabel6.text = userFieldsList[5].FieldTitle
        holder.userDefinedLabel7.text = userFieldsList[6].FieldTitle
        holder.userDefinedLabel8.text = userFieldsList[7].FieldTitle
        holder.userDefinedLabel9.text = userFieldsList[8].FieldTitle
        holder.userDefinedLabel10.text = userFieldsList[9].FieldTitle

        holder.userDefinedLabel1b.text = userFieldsList[0].FieldTitle
        holder.userDefinedLabel2b.text = userFieldsList[1].FieldTitle
        holder.userDefinedLabel3b.text = userFieldsList[2].FieldTitle
        holder.userDefinedLabel4b.text = userFieldsList[3].FieldTitle
        holder.userDefinedLabel5b.text = userFieldsList[4].FieldTitle
        holder.userDefinedLabel6b.text = userFieldsList[5].FieldTitle
        holder.userDefinedLabel7b.text = userFieldsList[6].FieldTitle
        holder.userDefinedLabel8b.text = userFieldsList[7].FieldTitle
        holder.userDefinedLabel9b.text = userFieldsList[8].FieldTitle
        holder.userDefinedLabel10b.text = userFieldsList[9].FieldTitle

        holder.userDefinedDescription1.text = userFieldsList[0].FieldDetails
        holder.userDefinedDescription2.text = userFieldsList[1].FieldDetails
        holder.userDefinedDescription3.text = userFieldsList[2].FieldDetails
        holder.userDefinedDescription4.text = userFieldsList[3].FieldDetails
        holder.userDefinedDescription5.text = userFieldsList[4].FieldDetails
        holder.userDefinedDescription6.text = userFieldsList[5].FieldDetails
        holder.userDefinedDescription7.text = userFieldsList[6].FieldDetails
        holder.userDefinedDescription8.text = userFieldsList[7].FieldDetails
        holder.userDefinedDescription9.text = userFieldsList[8].FieldDetails
        holder.userDefinedDescription10.text = userFieldsList[9].FieldDetails

        // Set visibility for fields that indicate an animal has left the herd
        if (current.Reason.isNullOrBlank()){
            holder.animalDateLeft.visibility = View.GONE
            holder.animalReason.visibility = View.GONE
            holder.animalDateLeftLabel.visibility = View.GONE
            holder.animalReasonLabel.visibility = View.GONE
        }
    }

    internal fun setOverviewList(overviewList: List<Cattle>) {
        this.overviewList = overviewList
        notifyDataSetChanged()
    }

    internal fun setUserFieldsList(userFieldsList: List<UserFields>) {
        this.userFieldsList = userFieldsList
        notifyDataSetChanged()
    }

    override fun getItemCount() = overviewList.size
}