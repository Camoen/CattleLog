package com.example.cattlelog

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cattlelog.dao.HealthDao
import com.example.cattlelog.database.CattlelogDatabase

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * All unit tests related to the HealthDao class.
 */
@RunWith(AndroidJUnit4::class)
class HealthDaoInstrumentedTest {
    private lateinit var healthDao: HealthDao
    private lateinit var db: CattlelogDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = CattlelogDatabase.getDatabase(context)
        healthDao = db.healthDao()
    }

    @Test
    @Throws(Exception::class)
    fun allHealthReportsReturned() {
        assertEquals(8445, healthDao.getAllHealthReports().size)
    }

    @Test
    @Throws(Exception::class)
    fun invalidTagNumberReturnsNoHealthReports() {
        assertEquals(0, healthDao.getAllHealthReportsWithTagNumber(-1)?.size)
    }

    @Test
    @Throws(Exception::class)
    fun validTagNumberReturnsHealthReports() {
        assertEquals(32, healthDao.getAllHealthReportsWithTagNumber(234)?.size)
    }

    @Test
    @Throws(Exception::class)
    fun validHealthEventReturnsHealthReports() {
        assertNotEquals(0, healthDao.getAllHealthReportsByEvent("041817 DWM    TOP IVERMECTIN")?.size)
    }

    @Test
    @Throws(Exception::class)
    fun validDateReturnsHealthReports() {
        assertNotEquals(0, healthDao.getAllHealthReportsForCattleBornOn("11/21/2015"))
    }
}
