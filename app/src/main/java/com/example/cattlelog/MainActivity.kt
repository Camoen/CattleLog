package com.example.cattlelog

import android.content.pm.PackageManager
import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.view.View
import com.example.cattlelog.database.CattlelogDatabase
import com.example.cattlelog.utility.Downloader
import java.io.*


private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "outputs"


class MainActivity : AppCompatActivity() {
    private lateinit var databaseDownloader: Downloader
    private lateinit var databaseDownloadFileName: String
    private var cachedDownloadID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseDownloader = Downloader(applicationContext, getString(R.string.db_URL))
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        downloadButton.setOnClickListener { takeActionWithPermission(it, ::startDownload) }
        deleteButton.setOnClickListener { takeActionWithPermission(it, ::removeFile) }
    }

    private fun takeActionWithPermission(view: View, actionToTake: () -> Unit ) {
        // Checks if version of Android is >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                // request permission if denied
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
                // requestPermissions(arrayOf(Manifest.permission_group.STORAGE), PERMISSION_CODE)
            } else {
                // If user allows apps to write to external storage (i.e., we don't need explicit permission)
                actionToTake()
            }
        } else {
            // OS is out of date, no permissions are needed
            actionToTake()
        }
    }

    private fun startDownload() {
        Toast.makeText(applicationContext, "Downloading...", Toast.LENGTH_SHORT).show()
        databaseDownloadFileName = "cattlelogdb_${System.currentTimeMillis()}.db"
        cachedDownloadID = databaseDownloader.downloadAs(databaseDownloadFileName)
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val incomingDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (incomingDownloadID == cachedDownloadID) {
                Toast.makeText(applicationContext, "Download completed.", Toast.LENGTH_SHORT).show()

                val downloadFile = File(Environment.getExternalStorageDirectory().toString() +
                                        File.separator + Environment.DIRECTORY_DOWNLOADS +
                                        File.separator + databaseDownloadFileName)

                val dbFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")

                try {
                    downloadFile.copyTo(dbFile, overwrite = true)
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
        }
    }

    // TODO: Remove unneeded files from the Downloads folder if possible (not important)
    // TODO: Maybe have removeFile() take the file to remove as its argument
    fun removeFile(){
        val fileToDelete = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")

        if (fileToDelete.exists()) {
            if (fileToDelete.delete()) {
                Toast.makeText(applicationContext, "Deleted ${fileToDelete.path}.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Unable to delete ${fileToDelete.path}.", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(applicationContext, "${fileToDelete.path} does not exist.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    startDownload()
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
