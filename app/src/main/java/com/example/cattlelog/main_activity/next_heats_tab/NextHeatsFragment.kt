package com.example.cattlelog.main_activity.next_heats_tab

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
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
import kotlinx.android.synthetic.main.next_heats_fragment.*

class NextHeatsFragment : Fragment(), NextHeatsListAdapter.RowListener {
    private lateinit var heatsViewModel: NextHeatsViewModel
    private lateinit var heatsRecyclerView: RecyclerView
    private lateinit var heatsListAdapter: NextHeatsListAdapter
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.next_heats_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = search

        heatsRecyclerView = nextExpectedHeatsList
        heatsListAdapter = NextHeatsListAdapter(
            activity as MainActivity,
            this
        )
        heatsRecyclerView.setHasFixedSize(true)
        heatsRecyclerView.adapter = heatsListAdapter
        heatsRecyclerView.layoutManager = LinearLayoutManager(activity as MainActivity)
        val divider = DividerItemDecoration(heatsRecyclerView.context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(
            ContextCompat.getDrawable(heatsRecyclerView.context,
                R.drawable.divider
            ) as Drawable
        )
        heatsRecyclerView.addItemDecoration(divider)

        heatsViewModel = ViewModelProvider(this).get(NextHeatsViewModel::class.java)
        heatsViewModel.allHeatsMembers.observe(this, Observer { cattleList ->
            cattleList?.let { heatsListAdapter.setCattleList(it) }
        })

        subscribeToSearches()
    }

    private fun subscribeToSearches() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Whenever the user types something in the search bar, we apply the filter.
            // See HerdListAdaptert's cattleFilter for how the filtering is actually done.
            override fun onQueryTextChange(newText: String): Boolean {
                heatsListAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })
    }

    override fun onRowClicked(position: Int) {
        val herdMember = heatsListAdapter.getCattleList()[position]
        val herdMemberDetailsIntent = Intent(activity as MainActivity, HerdMemberDetails::class.java)
        herdMemberDetailsIntent.putExtra(HERD_MEMBER_TAG, herdMember.TagNumber)
        herdMemberDetailsIntent.putExtra(HERD_MEMBER_BIRTHDATE, herdMember.BirthDate)
        startActivity(herdMemberDetailsIntent)
    }
}
