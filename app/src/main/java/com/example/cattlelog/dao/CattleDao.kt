package com.example.cattlelog.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.entities.Cattle

@Dao
interface CattleDao {
    @Query("SELECT * FROM Cattle")
    fun getAllCattle(): List<Cattle>

    @Query("SELECT * FROM Cattle WHERE TagNumber = :tagNumber")
    fun getCattleWithTagNumber(tagNumber: Int): Cattle?
}