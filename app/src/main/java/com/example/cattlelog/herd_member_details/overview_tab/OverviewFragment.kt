package com.example.cattlelog.herd_member_details.overview_tab


import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.cattlelog.R
import com.example.cattlelog.herd_member_details.HerdMemberDetails
import kotlinx.android.synthetic.main.fragment_overview.*
import pl.polidea.view.ZoomView

/**
 * A simple [Fragment] subclass.
 */
class OverviewFragment : Fragment() {

    private var herdMemberTagNumber: Int = -1
    private var herdMemberBirthDate: String = ""
    private lateinit var overviewViewModel: OverviewViewModel
    private lateinit var overviewViewModelFactory: OverviewViewModelFactory
    private lateinit var overviewRecyclerView: RecyclerView
    private lateinit var overviewAdapter: OverviewListAdapter
    private lateinit var zoomView: ZoomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        herdMemberTagNumber = (activity as HerdMemberDetails).getTagNumber()
        herdMemberBirthDate = (activity as HerdMemberDetails).getBirthDate()
//        Toast.makeText(context, "OverviewFragment received: $herdMemberTagNumber", Toast.LENGTH_LONG).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // TODO: (nice to have) Make it so we can pan left and right without switching tabs.  Currently must swipe up/down before swiping left/right
        val v = inflater.inflate(R.layout.fragment_overview, container, false)
        zoomView = ZoomView(context)
        zoomView.addView(v)

        return zoomView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        overviewAdapter =
            OverviewListAdapter(activity!!)
        overviewRecyclerView = overviewList
        overviewRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        overviewRecyclerView.setHasFixedSize(true)
        overviewRecyclerView.adapter = overviewAdapter
        val divider = DividerItemDecoration(overviewRecyclerView.context, DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(activity!!, R.drawable.divider) as Drawable)
        overviewRecyclerView.addItemDecoration(divider)


        // overviewViewModelFactory takes in extra parameters (TagNumber, BirthDate) and passes them to overviewViewModel
        overviewViewModelFactory =
            OverviewViewModelFactory(
                activity!!.application,
                herdMemberTagNumber,
                herdMemberBirthDate
            )
        overviewViewModel = ViewModelProvider(this, overviewViewModelFactory).get(
            OverviewViewModel::class.java)
        overviewViewModel.allAnimalDetails.observe(this, Observer { overviewList ->
            overviewList?.let { overviewAdapter.setOverviewList(it) }
        })
        overviewViewModel.allUserFields.observe(this, Observer { userFieldsList ->
            userFieldsList?.let { overviewAdapter.setUserFieldsList(it) }
        })
    }

}
