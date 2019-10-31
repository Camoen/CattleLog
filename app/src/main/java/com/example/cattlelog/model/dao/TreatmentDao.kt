package com.example.cattlelog.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.model.entities.Cattle
import com.example.cattlelog.model.entities.Treatment

@Dao
interface TreatmentDao {
    @Query("SELECT * FROM treatment")
    fun getAllTreatments(): List<Treatment>

    @Query("SELECT * FROM treatment WHERE TagNumber = :tagNumber")
    fun getAllTreatmentsByTagNumber(tagNumber: Int): List<Treatment>?

    @Query("SELECT * FROM treatment WHERE BirthDate = :birthDate")
    fun getAllTreatmentsByBirthDate(birthDate: String): List<Treatment>?

    @Query("SELECT * FROM treatment WHERE TagNumber = :tagNumber AND BirthDate = :birthDate ORDER BY RecordNumber DESC")
    fun getUniqueTreatmentData(tagNumber: Int, birthDate: String): LiveData<List<Treatment>>
}