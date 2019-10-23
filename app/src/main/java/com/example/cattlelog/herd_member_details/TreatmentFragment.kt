package com.example.cattlelog.herd_member_details


import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.cattlelog.herd.TreatmentViewModel
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.R
import com.example.cattlelog.herd.TreatmentListAdapter

import com.example.cattlelog.model.database.CattlelogDatabase
import kotlinx.android.synthetic.main.fragment_treatment.*


/**
 * A simple [Fragment] subclass.
 */

class TreatmentFragment : Fragment() {

    private var herdMemberTagNumber: Int = -1
    private var herdMemberBirthDate: String = ""
    private lateinit var treatmentViewModel: TreatmentViewModel
    private lateinit var treatmentRecyclerView: RecyclerView
    private lateinit var treatmentAdapter: TreatmentListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        herdMemberTagNumber = (activity as HerdMemberDetails).getTagNumber()
        herdMemberBirthDate = (activity as HerdMemberDetails).getBirthDate()

        // Use of "activity!!" is required to guarantee that "activity" will never be null. Calling with "activity" does not work.
        AsyncTask.execute {
            Log.d(
                "TEST QUERY IN FRAGMENT",
                "TREATMENT Test Query: " + CattlelogDatabase.getDatabase(activity!!).treatmentDao().getUniqueTreatmentData(
                    herdMemberTagNumber, herdMemberBirthDate
                )
            )
        }

        treatmentAdapter = TreatmentListAdapter(activity!!)
        treatmentRecyclerView = treatmentList
        treatmentRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        treatmentRecyclerView.setHasFixedSize(true)
        treatmentRecyclerView.adapter = treatmentAdapter
        val divider = DividerItemDecoration(treatmentRecyclerView.context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider) as Drawable)
        treatmentRecyclerView.addItemDecoration(divider)

        treatmentViewModel = ViewModelProvider(this).get(TreatmentViewModel(activity!!.application,herdMemberTagNumber,herdMemberBirthDate)::class.java)
        treatmentViewModel.allTreatments.observe(this, Observer { treatmentList ->
            treatmentList?.let { treatmentAdapter.setTreatmentList(it) }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.example.cattlelog.R.layout.fragment_treatment, container, false)
    }


}
