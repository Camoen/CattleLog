package com.example.cattlelog.database

import android.content.Context
import android.graphics.Typeface.createFromAsset
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cattlelog.dao.CattleDao
import com.example.cattlelog.dao.HealthDao
import com.example.cattlelog.dao.TreatmentDao
import com.example.cattlelog.dao.UserFieldsDao
import com.example.cattlelog.entities.Cattle
import com.example.cattlelog.entities.Health
import com.example.cattlelog.entities.Treatment
import com.example.cattlelog.entities.UserFields
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

        fun getDatabase(context: Context, databaseFile: File? = null): CattlelogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance: CattlelogDatabase

                if (databaseFile !== null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CattlelogDatabase::class.java,
                        DATABASE_NAME
                    ).createFromFile(databaseFile).build()
                } else {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CattlelogDatabase::class.java,
                        DATABASE_NAME
                    ).createFromAsset("databases/$DATABASE_NAME.db").build()
                }

                INSTANCE = instance
                return instance
            }
        }
    }
}