package com.example.cattlelog.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val TABLE_NAME = "Cattle"

@Entity (tableName = TABLE_NAME, primaryKeys = ["TagNumber", "BirthDate"])
data class Cattle(@ColumnInfo(name = "TagNumber") val TagNumber: Int,
                  @ColumnInfo(name = "BirthDate") val BirthDate: String,
                  @ColumnInfo(name = "DaysSinceBredHeat") val DaysSinceBredHeat: Int,
                  @ColumnInfo(name = "DaysTilDue") val DaysTilDue: Int,
                  @ColumnInfo(name = "GroupNumber") val GroupNumber: Int,
                  @ColumnInfo(name = "TempGroupNumber") val TempGroupNumber: Int,
                  @ColumnInfo(name = "ReproCode") val ReproCode: String,
                  @ColumnInfo(name = "TimesBred") val TimesBred: Int,
                  @ColumnInfo(name = "Breed") val Breed: String,
                  @ColumnInfo(name = "UsrDef1") val UsrDef1: String,
                  @ColumnInfo(name = "UsrDef2") val UsrDef2: String,
                  @ColumnInfo(name = "UsrDef3") val UsrDef3: String,
                  @ColumnInfo(name = "UsrDef4") val UsrDef4: String,
                  @ColumnInfo(name = "UsrDef5") val UsrDef5: String,
                  @ColumnInfo(name = "UsrDef6") val UsrDef6: String,
                  @ColumnInfo(name = "UsrDef7") val UsrDef7: String,
                  @ColumnInfo(name = "UsrDef8") val UsrDef8: String,
                  @ColumnInfo(name = "UsrDef9") val UsrDef9: String,
                  @ColumnInfo(name = "UsrDef10") val UsrDef10: String,
                  @ColumnInfo(name = "DaysTilNextHeat") val DaysTilNextHeat: Int,
                  @ColumnInfo(name = "BarnName") val BarnName: String,
                  @ColumnInfo(name = "DHIID") val DHIID: String,
                  @ColumnInfo(name = "DamIndex") val DamIndex: Int,
                  @ColumnInfo(name = "DamName") val DamName: String,
                  @ColumnInfo(name = "SireNameCode") val SireNameCode: String,
                  @ColumnInfo(name = "TimesBredDate") val TimesBredDate: String,
                  @ColumnInfo(name = "DateDue") val DateDue: String,
                  @ColumnInfo(name = "ServiceSireNameCode") val ServiceSireNameCode: String,
                  @ColumnInfo(name = "NextExpHeat") val NextExpHeat: String,
                  @ColumnInfo(name = "AgeInMonthsAtCalving") val AgeInMonthsAtCalving: Int,
                  @ColumnInfo(name = "DonorDamID") val DonorDamID: String,
                  @ColumnInfo(name = "FarmID") val FarmID: String,
                  @ColumnInfo(name = "DamDHI_ID") val DamDHI_ID: String,
                  @ColumnInfo(name = "PrevBredHeat1") val PrevBredHeat1: String,
                  @ColumnInfo(name = "PrevBredHeat2") val PrevBredHeat2: String,
                  @ColumnInfo(name = "PrevBredHeat3") val PrevBredHeat3: String,
                  @ColumnInfo(name = "WeightBirth") val WeightBirth: Int,
                  @ColumnInfo(name = "WeightWean") val WeightWean: Int,
                  @ColumnInfo(name = "WeightBred") val WeightBred: Int,
                  @ColumnInfo(name = "WeightPuberty") val WeightPuberty: Int,
                  @ColumnInfo(name = "WeightCalving") val WeightCalving: Int,
                  @ColumnInfo(name = "DaysInCurGroup") val DaysInCurGroup: Int,
                  @ColumnInfo(name = "DateLeft") val DateLeft: String,
                  @ColumnInfo(name = "Reason") val Reason: String
                  )