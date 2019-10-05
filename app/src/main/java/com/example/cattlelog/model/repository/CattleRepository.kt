package com.example.cattlelog.model.repository

import androidx.lifecycle.LiveData
import com.example.cattlelog.model.dao.CattleDao
import com.example.cattlelog.model.entities.Cattle

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CattleRepository(private val cattleDao: CattleDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCattle: LiveData<List<Cattle>> = cattleDao.getAllCattle()
}