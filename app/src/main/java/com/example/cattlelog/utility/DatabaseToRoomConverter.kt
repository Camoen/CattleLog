package com.example.cattlelog.utility

import android.content.Context
import android.util.Log
import java.io.FileOutputStream
import java.io.IOException
import androidx.room.Room
import com.example.cattlelog.database.CattlelogDatabase
import com.example.cattlelog.database.CattlelogDatabase.Companion.DATABASE_NAME

/*
    Credit: https://android.jlelse.eu/room-persistence-library-with-pre-populated-database-5f17ef103d3d
    Code converted to Kotlin. Some modifications made.
 */

class DatabaseToRoomConverter private constructor() {
    val roomDatabase: CattlelogDatabase

    init {
        //call method that check if database not exists and copy prepopulated file from assets
        copyAttachedDatabase(appContext!!, DATABASE_NAME)
        roomDatabase = Room.databaseBuilder(
            appContext!!,
            CattlelogDatabase::class.java, DATABASE_NAME
        ).allowMainThreadQueries().addMigrations(CattlelogDatabase.MIGRATION_1_to_2).build()
    }


    private fun copyAttachedDatabase(context: Context, databaseName: String) {
        val dbPath = context.getDatabasePath(databaseName)

        // If the database already exists, return
        if (dbPath.exists()) {
            return
        }

        // Make sure we have a path to the file
        dbPath.parentFile.mkdirs()

        // Try to copy database file
        try {
            val inputStream = context.assets.open("databases/$databaseName.db")
            val output = FileOutputStream(dbPath)

            val buffer = ByteArray(8192)
            var length: Int

            while (true) {
                length = inputStream.read(buffer, 0, BUFFER_SIZE)
                if (length <= 0) break else output.write(buffer, 0, length)
            }

            output.flush()
            output.close()
            inputStream.close()
        } catch (e: IOException) {
            Log.d(TAG, "Failed to open database file", e)
            e.printStackTrace()
        }

    }

    companion object {
        const val BUFFER_SIZE = 8192
        private val TAG = DatabaseToRoomConverter::class.java.simpleName
        private var appContext: Context? = null

        @Volatile
        private var INSTANCE: DatabaseToRoomConverter? = null

        fun getDatabase(context: Context): DatabaseToRoomConverter {
            appContext = context
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = DatabaseToRoomConverter()
                INSTANCE = instance
                return instance
            }
        }
    }
}