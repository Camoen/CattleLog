package com.example.cattlelog

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cattlelog.database.CattlelogDatabase
import com.example.cattlelog.entities.Cattle


// Class extends AndroidViewModel and requires application as a parameter.
class CattleViewModel(application: Application) : AndroidViewModel(application) {

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