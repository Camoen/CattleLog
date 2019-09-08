package com.example.cattlelog.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

public abstract class CattlelogDatabase : RoomDatabase() {

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