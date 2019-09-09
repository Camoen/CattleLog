package com.example.cattlelog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cattlelog.dao.CattleDao
import com.example.cattlelog.entities.Cattle

@Database(entities = arrayOf(Cattle::class), version = 1)
public abstract class CattlelogDatabase : RoomDatabase() {

    abstract fun cattleDao(): CattleDao

    // Singleton pattern to prevent multiple instances of DB being open concurrently
    // Credit: https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#6
    companion object {
        const val DATABASE_NAME = "cattlelog_database"

        val MIGRATION_1_to_2 : Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }

        @Volatile
        private var INSTANCE: CattlelogDatabase? = null

        fun getDatabase(context: Context): CattlelogDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CattlelogDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}