package com.example.cattlelog.main_activity.herd_tab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.model.entities.Cattle

class HerdViewModel(application: Application) : AndroidViewModel(application) {
    val allHerdMembers: LiveData<List<Cattle>> = CattlelogDatabase.getDatabase(application).cattleDao().getAllCattle()
}
