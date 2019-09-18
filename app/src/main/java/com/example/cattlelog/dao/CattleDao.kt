package com.example.cattlelog.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.entities.Cattle

@Dao
interface CattleDao {
    @Query("SELECT * FROM cattle")
    fun getAllCattle(): List<Cattle>

    @Query("SELECT * FROM cattle WHERE TagNumber = :tagNumber")
    fun getCattleWithTagNumber(tagNumber: Int): Cattle?
}