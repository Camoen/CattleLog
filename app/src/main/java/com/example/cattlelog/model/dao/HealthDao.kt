package com.example.cattlelog.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.model.entities.Health

@Dao
interface HealthDao {
    @Query("SELECT * FROM health")
    fun getAllHealthReports(): List<Health>

    @Query("SELECT * FROM health WHERE TagNumber = :tagNumber")
    fun getAllHealthReportsWithTagNumber(tagNumber: Int): List<Health>?

    @Query("SELECT * FROM health WHERE BirthDate = :date")
    fun getAllHealthReportsForCattleBornOn(date: String): List<Health>?

    @Query("SELECT * FROM health WHERE HealthEvent = :event")
    fun getAllHealthReportsByEvent(event: String): List<Health>?

    @Query("SELECT * FROM health WHERE TagNumber = :tagNumber AND BirthDate = :birthDate " +
            "ORDER BY (substr(HealthEvent,5,2)||substr(HealthEvent,1,2)||substr(HealthEvent,3,2)) DESC")
    fun getUniqueHealthData(tagNumber: Int, birthDate: String): LiveData<List<Health>>
}