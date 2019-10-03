package com.example.cattlelog.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "treatment",
        primaryKeys = ["TagNumber", "BirthDate", "RecordNumber"],
        foreignKeys = [ForeignKey(entity = Cattle::class,
        parentColumns = ["TagNumber", "BirthDate"],
        childColumns = ["TagNumber", "BirthDate"],
        onDelete = ForeignKey.CASCADE)])
data class Treatment(@ColumnInfo(name = "TagNumber") val TagNumber: Int,
                     @ColumnInfo(name = "BirthDate") val BirthDate: String,
                     @ColumnInfo(name = "RecordNumber") val RecordNumber: Int,
                     @ColumnInfo(name = "EventDate") val EventDate: String? = null,
                     @ColumnInfo(name = "PinkEye") val PinkEye: String? = null,
                     @ColumnInfo(name = "EyeSide") val EyeSide: String? = null,
                     @ColumnInfo(name = "Respiratory") val Respiratory: String? = null,
                     @ColumnInfo(name = "Scours") val Scours: String? = null,
                     @ColumnInfo(name = "Foot") val Foot: String? = null,
                     @ColumnInfo(name = "FootPosition") val FootPosition: String? = null,
                     @ColumnInfo(name = "Mastitis") val Mastitis: String? = null,
                     @ColumnInfo(name = "Other") val Other: String? = null,
                     @ColumnInfo(name = "Details") val Details: String? = null)