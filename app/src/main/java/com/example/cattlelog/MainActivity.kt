package com.example.cattlelog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cattlelog.database.CattlelogDatabase
import android.os.AsyncTask


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AsyncTask.execute {
            val allCattle = CattlelogDatabase.getDatabase(this).cattleDao().getAllCattle()
            Log.d("TESTING123", allCattle.toString())
        }
    }
}
