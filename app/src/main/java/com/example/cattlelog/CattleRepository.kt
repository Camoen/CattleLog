package com.example.cattlelog

import androidx.lifecycle.LiveData
import com.example.cattlelog.dao.CattleDao
import com.example.cattlelog.entities.Cattle

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class CattleRepository(private val cattleDao: CattleDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allCattle: LiveData<List<Cattle>> = cattleDao.getAllCattle()

}