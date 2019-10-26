package com.example.cattlelog.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.example.cattlelog.model.entities.UserFields

@Dao
interface UserFieldsDao {
    @Query("SELECT * FROM userFields")
    fun getAllUserFields(): LiveData<List<UserFields>>

    @Query("SELECT * FROM userFields WHERE FieldNumber = :fieldNumber")
    fun getUserFieldByFieldNumber(fieldNumber: Int): UserFields?
}