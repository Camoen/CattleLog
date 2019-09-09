package com.example.cattlelog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cattlelog.database.CattlelogDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("PLEASE WORK", CattlelogDatabase.getDatabase(this).cattleDao().getAllCattle().toString())
    }
}
