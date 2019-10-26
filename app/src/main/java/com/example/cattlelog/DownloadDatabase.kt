package com.example.cattlelog

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.example.cattlelog.main_activity.TARGET_FILE_KEY
import com.example.cattlelog.model.database.CattlelogDatabase.Companion.DATABASE_NAME
import com.example.cattlelog.utility.Downloader
import java.io.File


private const val LOG_TAG = "DownloadDatabase"


class DownloadDatabase : AppCompatActivity() {

    private lateinit var databaseDownloader: Downloader
    private lateinit var tempDownloadFile: File
    private lateinit var targetFile: File
    private var cachedDownloadID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_database)
        title = "Database Download"

        targetFile = intent.getExtras().get(TARGET_FILE_KEY) as File
        Log.d(LOG_TAG, targetFile.toString())
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
        tempDownloadFile = File(Environment.getExternalStorageDirectory().toString() +
                File.separator + Environment.DIRECTORY_DOWNLOADS +
                File.separator + "cattlelogdb_${System.currentTimeMillis()}.db")
        cachedDownloadID = databaseDownloader.download(DATABASE_NAME, tempDownloadFile)
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val incomingDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (incomingDownloadID == cachedDownloadID) {
                Toast.makeText(applicationContext, "Download completed.", Toast.LENGTH_SHORT).show()
                copyToTargetFile()
            }

            finish()
        }
    }

    private fun copyToTargetFile() {
        try {
            tempDownloadFile.copyTo(targetFile, overwrite = true)
            tempDownloadFile.delete()
            Log.d(LOG_TAG, "File has been copied successfully.")
            // Activity result must be marked as OK, otherwise it default returns RESULT_CANCELED
            setResult(Activity.RESULT_OK)
        } catch (e: Exception) {
            Log.d(LOG_TAG, "Error copying file.")
            Log.e(LOG_TAG, "exception", e)
        }
    }


}
