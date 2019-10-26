package com.example.cattlelog.main_activity.herd_tab

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.cattlelog.R

class HerdFragment : Fragment() {

    companion object {
        fun newInstance() = HerdFragment()
    }

    private lateinit var viewModel: HerdViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.herd_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HerdViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
