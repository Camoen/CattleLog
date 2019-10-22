package com.example.cattlelog.herd_member_details

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cattlelog.R
import com.example.cattlelog.herd_member_details.HealthFragment
import com.example.cattlelog.herd_member_details.OverviewFragment
import com.example.cattlelog.herd_member_details.Tab
import com.example.cattlelog.herd_member_details.TreatmentFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment

        when (position) {
            Tab.OVERVIEW.position -> fragment = OverviewFragment()
            Tab.HEALTH.position -> fragment = HealthFragment()
            Tab.TREATMENT.position -> fragment = TreatmentFragment()
            // So Kotlin will stop complaining >:(
            else -> fragment = OverviewFragment()
        }

        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(Tab[position]!!.title)
    }

    // Return the number of tabs
    override fun getCount(): Int {
        return Tab.values().size
    }
}