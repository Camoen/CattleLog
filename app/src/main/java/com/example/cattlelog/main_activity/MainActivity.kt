package com.example.cattlelog.main_activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

const val PREFS_FILENAME = "com.example.cattlelog.shared_preferences"
const val DB_VERSION = "database_version"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.cattlelog.R.layout.activity_main)
        val sectionsPagerAdapter =
            TabPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(com.example.cattlelog.R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(com.example.cattlelog.R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}
