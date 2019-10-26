package com.example.cattlelog.herd_member_details.health_tab


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cattlelog.R
import com.example.cattlelog.herd_member_details.HerdMemberDetails
import kotlinx.android.synthetic.main.fragment_health.*


/**
 * A simple [Fragment] subclass.
 */

class HealthFragment : Fragment() {

    private var herdMemberTagNumber: Int = -1
    private var herdMemberBirthDate: String = ""
    private lateinit var healthViewModel: HealthViewModel
    private lateinit var healthViewModelFactory: HealthViewModelFactory
    private lateinit var healthRecyclerView: RecyclerView
    private lateinit var healthAdapter: HealthListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        herdMemberTagNumber = (activity as HerdMemberDetails).getTagNumber()
        herdMemberBirthDate = (activity as HerdMemberDetails).getBirthDate()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        healthAdapter =
            HealthListAdapter(activity!!)
        healthRecyclerView = healthList
        healthRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        healthRecyclerView.setHasFixedSize(true)
        healthRecyclerView.adapter = healthAdapter
        val divider = DividerItemDecoration(healthRecyclerView.context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider) as Drawable)
        healthRecyclerView.addItemDecoration(divider)


        // healthViewModelFactory takes in extra parameters (TagNumber, BirthDate) and passes them to healthViewModel
        healthViewModelFactory =
            HealthViewModelFactory(
                activity!!.application,
                herdMemberTagNumber,
                herdMemberBirthDate
            )
        healthViewModel = ViewModelProvider(this, healthViewModelFactory).get(HealthViewModel::class.java)
        healthViewModel.allHealth.observe(this, Observer { healthList ->
            healthList?.let { healthAdapter.setHealthList(it) }
        })
    }


}
