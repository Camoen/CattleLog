package com.example.cattlelog.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.entities.UserFields

@Dao
interface UserFieldsDao {
    @Query("SELECT * FROM userFields")
    fun getAllUserFields(): List<UserFields>

    @Query("SELECT * FROM userFields WHERE FieldNumber = :fieldNumber")
    fun getUserFieldByFieldNumber(fieldNumber: Int): UserFields?
}