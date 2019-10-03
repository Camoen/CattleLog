package com.example.cattlelog

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cattlelog.model.dao.TreatmentDao
import com.example.cattlelog.model.database.CattlelogDatabase
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * All unit tests related to the TreatmentDao class.
 */
@RunWith(AndroidJUnit4::class)
class TreatmentDaoInstrumentedTest {
    private lateinit var treatmentDao: TreatmentDao
    private lateinit var db: CattlelogDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = CattlelogDatabase.getDatabase(context)
        treatmentDao = db.treatmentDao()
    }

    @Test
    @Throws(Exception::class)
    fun allHealthReportsReturned() {
        assertEquals(720, treatmentDao.getAllTreatments().size)
    }

    @Test
    @Throws(Exception::class)
    fun invalidTagNumberReturnsNoHealthReports() {
        assertEquals(0, treatmentDao.getAllTreatmentsByTagNumber(-1)?.size)
    }

    @Test
    @Throws(Exception::class)
    fun invalidBirthDateReturnsNoHealthReports() {
        assertEquals(0, treatmentDao.getAllTreatmentsByBirthDate("07/08/2007")?.size)
    }

    @Test
    @Throws(Exception::class)
    fun validTagNumberReturnsCorrectHealthReports() {
        assertEquals(3, treatmentDao.getAllTreatmentsByTagNumber(461)?.size)
    }
}