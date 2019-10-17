package com.example.cattlelog.herd_member_details

import android.os.Bundle
import android.widget.TextView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.example.cattlelog.R

const val HERD_MEMBER_TAG = "HERD MEMBER TAG"

class HerdMemberDetails : AppCompatActivity() {

    private var tagNumber: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_herd_member_details)
        tagNumber = intent?.extras?.getInt(HERD_MEMBER_TAG) as Int
        findViewById<TextView>(R.id.title).text = String.format(getString(R.string.member_number), tagNumber)

        val sectionsPagerAdapter =
            TabPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    fun getTagNumber(): Int {
        return tagNumber
    }
}