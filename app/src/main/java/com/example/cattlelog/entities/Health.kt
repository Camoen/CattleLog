package com.example.cattlelog.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity (tableName = "health",
         primaryKeys = ["TagNumber", "BirthDate", "HealthEvent"],
         foreignKeys = [ForeignKey(entity = Cattle::class,
                                   parentColumns = ["TagNumber", "BirthDate"],
                                   childColumns = ["TagNumber", "BirthDate"],
                                   onDelete = ForeignKey.CASCADE)])
data class Health(@ColumnInfo(name = "TagNumber") val TagNumber: Int,
                  @ColumnInfo(name = "BirthDate") val BirthDate: String,
                  @ColumnInfo(name = "HealthEvent") val HealthEvent: String)