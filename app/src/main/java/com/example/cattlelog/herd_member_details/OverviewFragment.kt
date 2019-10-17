package com.example.cattlelog.herd_member_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.example.cattlelog.R

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    private var herdMemberTagNumber: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        herdMemberTagNumber = (activity as HerdMemberDetails).getTagNumber()
        Toast.makeText(context, "OverviewFragment received: ${herdMemberTagNumber}", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }
}
