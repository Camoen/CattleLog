package com.example.cattlelog.main_activity.next_heats_tab

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.cattlelog.R

class NextHeatsFragment : Fragment() {

    companion object {
        fun newInstance() = NextHeatsFragment()
    }

    private lateinit var viewModel: NextHeatsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_heats_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(NextHeatsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
