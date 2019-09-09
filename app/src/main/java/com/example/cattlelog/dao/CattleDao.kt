package com.example.cattlelog.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.entities.Cattle

@Dao
interface CattleDao {
    @Query("SELECT * from Cattle")
    fun getAllCattle(): List<Cattle>
}