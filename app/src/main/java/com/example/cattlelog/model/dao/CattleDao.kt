package com.example.cattlelog.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.model.entities.Cattle

@Dao
interface CattleDao {
    @Query("SELECT * FROM cattle ORDER BY TagNumber ASC")
    fun getAllCattle(): LiveData<List<Cattle>>

    @Query("SELECT * FROM cattle WHERE TagNumber = :tagNumber  ORDER BY BirthDate ASC")
    fun getCattleWithTagNumber(tagNumber: Int): Cattle?
//    fun getCattleWithTagNumber(tagNumber: Int): List<Cattle>?


//    TODO: We can add custom intervals for next expected heats report.  This would take in a day range (3 days into past and 4 days into future, for example)
    // Check the query below to see how it would be formulated.  I put FUTURE (imagine +4) and PAST (imagine 3) as placeholders for the formula required
    // This is a BONUS feature, we can just use `getNextExpectedHeatsPreset()` for now.
//    @Query("select * from cattle where DATE(NextExpHeat) BETWEEN DATE(datetime('now','localtime', '-PAST day')) AND DATE(datetime('now','localtime', 'FUTURE day'))\n" +
//            "UNION\n" +
//            "select * from cattle where DATE(NextExpHeat) BETWEEN DATE(datetime('now','localtime', '21-PAST day')) AND DATE(datetime('now','localtime', '21 day'));")
//    fun getNextExpectedHeats(): Cattle?

    // For Testing Only (returns a result)
    //@Query("select * from cattle where DATE(NextExpHeat) BETWEEN DATE(datetime('2020-08-29')) AND DATE(datetime('2020-09-16'))")
    @Query("select * from cattle where DATE(NextExpHeat) BETWEEN DATE(datetime('now','localtime', '+0 day')) AND DATE(datetime('now','localtime', '+365 day'))")
    fun getNextExpectedHeatsTEST(): List<Cattle>?

    @Query("select * from cattle where DATE(NextExpHeat) BETWEEN DATE(datetime('now','localtime', '-3 day')) AND DATE(datetime('now','localtime', '3 day'))\n" +
            "UNION\n" +
            "select * from cattle where DATE(NextExpHeat) BETWEEN DATE(datetime('now','localtime', '18 day')) AND DATE(datetime('now','localtime', '21 day'));")
    fun getNextExpectedHeatsPreset(): List<Cattle>?
}