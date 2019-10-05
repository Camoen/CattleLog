package com.example.cattlelog.model.dao

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
}