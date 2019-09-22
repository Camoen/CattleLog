package com.example.cattlelog

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cattlelog.dao.UserFieldsDao
import com.example.cattlelog.database.CattlelogDatabase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * All unit tests related to the UserFieldsDao class.
 */
@RunWith(AndroidJUnit4::class)
class UserFieldsDaoInstrumentedTest {
    private lateinit var userFieldsDao: UserFieldsDao
    private lateinit var db: CattlelogDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = CattlelogDatabase.getDatabase(context)
        userFieldsDao = db.userFieldsDao()
    }

    @Test
    @Throws(Exception::class)
    fun allUserFieldsAreReturned() {
        assertEquals(10, userFieldsDao.getAllUserFields().size)
    }

    @Test
    @Throws(Exception::class)
    fun invalidFieldNumberReturnsNoFields() {
        assertEquals(null, userFieldsDao.getUserFieldByFieldNumber(0))
    }

    @Test
    @Throws(Exception::class)
    fun expectedFieldTitleMatches() {
        assertEquals("Calving Date", userFieldsDao.getUserFieldByFieldNumber(2)?.FieldTitle)
    }

    @Test
    @Throws(Exception::class)
    fun expectedFieldDetailsMatch() {
        assertEquals("Blood sample: B / Blood Card: BC / Sample & Card: B+C / Sample needed: NEE",
                      userFieldsDao.getUserFieldByFieldNumber(4)?.FieldDetails)
    }
}