package com.example.cattlelog.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "userFields", primaryKeys = ["FieldNumber"])
data class UserFields(@ColumnInfo(name = "FieldNumber") val FieldNumber: Int,
                      @ColumnInfo(name = "FieldTitle") val FieldTitle: String? = null,
                      @ColumnInfo(name = "FieldDetails") val FieldDetails: String? = null)