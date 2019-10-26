package com.example.cattlelog.main_activity.herd_tab

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cattlelog.R
import com.example.cattlelog.herd_member_details.HERD_MEMBER_BIRTHDATE
import com.example.cattlelog.herd_member_details.HERD_MEMBER_TAG
import com.example.cattlelog.herd_member_details.HerdMemberDetails
import com.example.cattlelog.main_activity.MainActivity
import kotlinx.android.synthetic.main.herd_fragment.*

class HerdFragment : Fragment(), HerdListAdapter.RowListener {
    private lateinit var herdViewModel: HerdViewModel
    private lateinit var cattleRecyclerView: RecyclerView
    private lateinit var herdListAdapter: HerdListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.herd_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cattleRecyclerView = herdList
        herdListAdapter = HerdListAdapter(
            activity as MainActivity,
            this
        )
        cattleRecyclerView.setHasFixedSize(true)
        cattleRecyclerView.adapter = herdListAdapter
        cattleRecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        val divider = DividerItemDecoration(cattleRecyclerView.context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(
            ContextCompat.getDrawable(cattleRecyclerView.context,
                R.drawable.divider
            ) as Drawable
        )
        cattleRecyclerView.addItemDecoration(divider)

        herdViewModel = ViewModelProvider(this).get(HerdViewModel::class.java)
        herdViewModel.allHerdMembers.observe(this, Observer { cattleList ->
            cattleList?.let { herdListAdapter.setCattleList(it) }
        })
    }

    override fun onRowClicked(position: Int) {
        val herdMember = herdListAdapter.getCattleList()[position]
        val herdMemberDetailsIntent = Intent(activity as MainActivity, HerdMemberDetails::class.java)
        herdMemberDetailsIntent.putExtra(HERD_MEMBER_TAG, herdMember.TagNumber)
        herdMemberDetailsIntent.putExtra(HERD_MEMBER_BIRTHDATE, herdMember.BirthDate)
        startActivity(herdMemberDetailsIntent)
    }
}
