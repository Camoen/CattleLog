package com.example.cattlelog

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cattlelog.database.CattlelogDatabase
import com.example.cattlelog.utility.Downloader
import java.io.File


private const val LOG_TAG = "DownloadDatabase"

class DownloadDatabase : AppCompatActivity() {

    private lateinit var databaseDownloader: Downloader
    private lateinit var databaseDownloadTempName: String
    private lateinit var taretFileName: String
    private var cachedDownloadID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_database)

        taretFileName = intent.getStringExtra(DESIRED_FILE_NAME_KEY)
        databaseDownloader = Downloader(applicationContext, getString(R.string.db_URL))
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        startDownload()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

    private fun startDownload() {
        Toast.makeText(applicationContext, "Downloading...", Toast.LENGTH_SHORT).show()
        databaseDownloadTempName = "cattlelogdb_${System.currentTimeMillis()}.db"
        Log.d("DownloadDatabase", databaseDownloadTempName)
        cachedDownloadID = databaseDownloader.downloadAs(databaseDownloadTempName)
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val incomingDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (incomingDownloadID == cachedDownloadID) {
                Toast.makeText(applicationContext, "Download completed.", Toast.LENGTH_SHORT).show()

                val tempDownloadFile = File(
                    Environment.getExternalStorageDirectory().toString() +
                            File.separator + Environment.DIRECTORY_DOWNLOADS +
                            File.separator + databaseDownloadTempName)

                Log.d("DownloadDatabase", tempDownloadFile.absolutePath)

                val dbFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")

                try {
                    tempDownloadFile.copyTo(dbFile, overwrite = true)
                    Log.d(LOG_TAG, "File has been copied successfully.")

                    // TODO replace later, this is only here for rough testing
                    AsyncTask.execute {
                        Log.d(
                            LOG_TAG,
                            "" + CattlelogDatabase.getDatabase(applicationContext, dbFile)
                                .cattleDao().getAllCattle()
                        )
                    }
                } catch (e: Exception) {
                    Log.d(LOG_TAG, "Error copying file.")
                    Log.e(LOG_TAG, "exception", e)
                }
            }

            finish()
        }
    }
}
