package com.example.cattlelog.main_activity.next_heats_tab

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

class NextHeatsListAdapter  internal constructor(
    context: Context,
    private var rowClickListener: RowListener
) :
    RecyclerView.Adapter<NextHeatsListAdapter.NextHeatsViewHolder>(), Filterable {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var cattleListFiltered = mutableListOf<Cattle>()
    private var cattleListUnfiltered = emptyList<Cattle>()

    inner class NextHeatsViewHolder(itemView: View, rowListener: RowListener) :
        RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val tagNumber: TextView = itemView.findViewById(R.id.nextExpHeatsTagNumber)
        val nextExpectedHeat: TextView = itemView.findViewById(R.id.nextExpectedHeat)
        val sireNameCode: TextView = itemView.findViewById(R.id.sireNameCode)
        val timesBredDate: TextView = itemView.findViewById(R.id.timesBredDate)
        private val rowClickListener: RowListener = rowListener

        init {
            itemView.setOnClickListener(this)
        }

        /**
         * Called when a user clicks one of the rows in the corresponding RecyclerView.
         * It essentially "passes on" the call to the associated class implementing RowListener.
         */
        override fun onClick(v: View?) {
            rowClickListener.onRowClicked(adapterPosition)
        }
    }

    // Classes will implement this interface to customize what happens when a row is clicked
    public interface RowListener {
        fun onRowClicked(position: Int);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NextHeatsViewHolder {
        val itemView = inflater.inflate(R.layout.next_heats_row, parent, false)
        return NextHeatsViewHolder(itemView, rowClickListener)
    }

    override fun onBindViewHolder(holder: NextHeatsViewHolder, position: Int) {
        val current = cattleListFiltered[position]
        holder.tagNumber.text = current.TagNumber.toString()
        holder.nextExpectedHeat.text = current.NextExpHeat
        holder.sireNameCode.text = current.SireNameCode
        holder.timesBredDate.text = current.TimesBredDate
    }

    internal fun setCattleList(cattleList: List<Cattle>) {
        this.cattleListFiltered = cattleList.toMutableList()
        // toList() ensures that we get a copy and not a reference
        this.cattleListUnfiltered = cattleList.toList()
        notifyDataSetChanged()
    }

    fun getCattleList(): MutableList<Cattle> {
        return this.cattleListFiltered
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
                    if (cattle.TagNumber.toString().contains(filterPattern)) {
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