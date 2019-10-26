package com.example.cattlelog.main_activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.model.entities.Cattle
import com.example.cattlelog.model.repository.CattleRepository


// Class extends AndroidViewModel and requires application as a parameter.
class HerdViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: CattleRepository
    // LiveData gives us updated cattle when they change.
    val allCattle: LiveData<List<Cattle>>

    init {
        // Gets reference to CattleDao from CattlelogDatabase to construct
        // the correct CattleRepository.
        val herdDao = CattlelogDatabase.getDatabase(application).cattleDao()
        repository = CattleRepository(herdDao)
        allCattle = repository.allCattle
    }

}