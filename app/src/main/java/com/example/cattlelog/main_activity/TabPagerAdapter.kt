package com.example.cattlelog.main_activity

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.cattlelog.main_activity.herd_tab.HerdFragment
import com.example.cattlelog.main_activity.next_heats_tab.NextHeatsFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class TabPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            Tab.HERD.position -> HerdFragment()
            Tab.NEXT_HEATS.position -> NextHeatsFragment()
            // So Kotlin will stop complaining >:(
            else -> HerdFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(Tab[position]!!.title)
    }

    // Return the number of tabs
    override fun getCount(): Int {
        return Tab.values().size
    }
}