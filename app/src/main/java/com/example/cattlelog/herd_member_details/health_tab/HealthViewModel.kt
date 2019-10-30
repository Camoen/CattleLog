package com.example.cattlelog.herd_member_details.health_tab

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.model.entities.Health


// Class extends AndroidViewModel and requires application as a parameter.
class HealthViewModel(application: Application, tagNumber: Int, birthDate: String) : AndroidViewModel(application) {

    // LiveData gives us updated treatments when they change.
    val allHealth: LiveData<List<Health>> = CattlelogDatabase.getDatabase(application).healthDao().getUniqueHealthData(tagNumber, birthDate)
}