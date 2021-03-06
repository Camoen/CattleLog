package com.example.cattlelog

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cattlelog.model.dao.CattleDao
import com.example.cattlelog.model.database.CattlelogDatabase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * All unit tests related to the CattleDao class.
 */
@RunWith(AndroidJUnit4::class)
class CattleDaoInstrumentedTest {
    private lateinit var cattleDao: CattleDao
    private lateinit var db: CattlelogDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = CattlelogDatabase.getDatabase(context)
        cattleDao = db.cattleDao()
    }

    @Test
    @Throws(Exception::class)
    fun invalidTagNumberReturnsNullCattle() {
        assertEquals(null, cattleDao.getCattleWithTagNumber(-1))
    }

    @Test
    @Throws(Exception::class)
    fun validTagNumberReturnsCorrectCattle() {
        assertEquals("07/28/2018", cattleDao.getCattleWithTagNumber(95684)?.BirthDate)
    }

    @Test
    @Throws(Exception::class)
    fun allCattleAreReturned() {
        assertEquals(767, cattleDao.getAllCattle().size)
    }
}
