package com.example.cattlelog.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cattlelog.model.dao.CattleDao
import com.example.cattlelog.model.dao.HealthDao
import com.example.cattlelog.model.dao.TreatmentDao
import com.example.cattlelog.model.dao.UserFieldsDao
import com.example.cattlelog.model.entities.Cattle
import com.example.cattlelog.model.entities.Health
import com.example.cattlelog.model.entities.Treatment
import com.example.cattlelog.model.entities.UserFields
import java.io.File

@Database(entities = [Cattle::class, Health::class, Treatment::class, UserFields::class], version = 1)
public abstract class CattlelogDatabase : RoomDatabase() {

    abstract fun cattleDao(): CattleDao
    abstract fun healthDao(): HealthDao
    abstract fun treatmentDao(): TreatmentDao
    abstract fun userFieldsDao(): UserFieldsDao

    // Singleton pattern to prevent multiple instances of DB being open concurrently
    companion object {
        const val DATABASE_NAME = "cattlelog_database"

        @Volatile
        private var INSTANCE: CattlelogDatabase? = null

        fun getDatabase(context: Context): CattlelogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val databaseFile = File(context.filesDir, "$DATABASE_NAME.db")
                val instance: CattlelogDatabase = Room.databaseBuilder(
                    context.applicationContext,
                    CattlelogDatabase::class.java,
                    DATABASE_NAME
                ).createFromFile(databaseFile).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}