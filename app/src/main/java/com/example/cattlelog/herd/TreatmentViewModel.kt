package com.example.cattlelog.herd

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.model.entities.Treatment


// Class extends AndroidViewModel and requires application as a parameter.
class TreatmentViewModel(application: Application, tagNumber: Int, birthDate: String) : AndroidViewModel(application) {

    // LiveData gives us updated treatments when they change.
    val allTreatments: LiveData<List<Treatment>>

    init {
        allTreatments = CattlelogDatabase.getDatabase(application).treatmentDao().getUniqueTreatmentData(tagNumber, birthDate)
    }

}