package com.example.cattlelog.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.entities.Treatment

@Dao
interface TreatmentDao {
    @Query("SELECT * FROM treatment")
    fun getAllTreatments(): List<Treatment>

    @Query("SELECT * FROM treatment WHERE TagNumber = :tagNumber")
    fun getAllTreatmentsByTagNumber(tagNumber: Int): List<Treatment>?

    @Query("SELECT * FROM treatment WHERE BirthDate = :birthDate")
    fun getAllTreatmentsByBirthDate(birthDate: String): List<Treatment>?
}