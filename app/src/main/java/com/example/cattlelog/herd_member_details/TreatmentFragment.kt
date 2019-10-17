package com.example.cattlelog.herd_member_details


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.cattlelog.R

/**
 * A simple [Fragment] subclass.
 */
class TreatmentFragment : Fragment() {

    private var herdMemberTagNumber: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        herdMemberTagNumber = (activity as HerdMemberDetails).getTagNumber()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_treatment, container, false)
    }
}