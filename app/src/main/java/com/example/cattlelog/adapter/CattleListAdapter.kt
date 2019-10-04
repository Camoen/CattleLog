package com.example.cattlelog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.R
import com.example.cattlelog.model.entities.Cattle
import java.util.*

class CattleListAdapter internal constructor(
    context: Context
) : RecyclerView.Adapter<CattleListAdapter.CattleViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cattleListFiltered = mutableListOf<Cattle>()
    private var cattleListUnfiltered = emptyList<Cattle>()

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
        val current = cattleListFiltered[position]
        holder.cattleItemView0.text = current.TagNumber.toString()
        holder.cattleItemView1.text = current.BirthDate
        holder.cattleItemView2.text = current.BarnName
    }

    internal fun setCattleList(cattleList: List<Cattle>) {
        this.cattleListFiltered = cattleList.toMutableList()
        // toList() ensures that we get a copy and not a reference
        this.cattleListUnfiltered = cattleList.toList()
        notifyDataSetChanged()
    }

    override fun getItemCount() = cattleListFiltered.size

    override fun getFilter(): Filter {
        return cattleFilter
    }

    private val cattleFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Cattle> = mutableListOf<Cattle>()

            // That is, if the user did not type anything into the search bar, just return the default unfiltered list
            if (constraint.isNullOrEmpty()) {
                filteredList.addAll(cattleListUnfiltered)
            } else {
                val filterPattern = constraint.toString().trim()

                for (cattle in cattleListUnfiltered) {
                    // == true may seem silly, but it's required because the BarnName expression technically
                    // returns Boolean? (nullable) instead of Boolean, which is what an if statement expects
                    if (cattle.BarnName?.contains(filterPattern, true) == true ||
                        cattle.BirthDate.contains(filterPattern, true) ||
                        cattle.TagNumber.toString().contains(filterPattern)) {
                        filteredList.add(cattle)
                    }
                }
            }

            val results: FilterResults = FilterResults()
            results.values = filteredList

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            cattleListFiltered.clear()
            cattleListFiltered.addAll(results?.values as MutableList<Cattle>)
            notifyDataSetChanged()
        }
    }
}