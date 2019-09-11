package com.example.cattlelog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.cattlelog.database.CattlelogDatabase
import android.os.AsyncTask
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.cattlelog.entities.Cattle
import com.example.cattlelog.utility.DatabaseToRoomConverter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AsyncTask.execute {
            val allCattle = DatabaseToRoomConverter.getDatabase(this).roomDatabase.cattleDao().getAllCattle()
            Log.d("MOO", allCattle.toString())
        }
    }
}
