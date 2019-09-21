package com.example.cattlelog

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import com.example.cattlelog.database.CattlelogDatabase
import java.io.*


private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "outputs"


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button Click Handler
        downloadButton.setOnClickListener {
            // Checks if version of Android is >= Marshmallow
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    // request permission if denied
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
//                    requestPermissions(arrayOf(Manifest.permission_group.STORAGE), PERMISSION_CODE)
                }
                else {
                    startDownload()
                }
            }
            else {
                // OS is out of date, no permissions are needed
                startDownload()
            }
        }

        // Button Click Handler
        deleteButton.setOnClickListener {
            // Checks if version of Android is >= Marshmallow
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                    // request permission if denied
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
                }
                else {
                    removeFile()
                }
            }
            else {
                // OS is out of date, no permissions are needed
                removeFile()
            }
        }
    }

    // Handles file download
    private fun startDownload() {
        val request = DownloadManager.Request(Uri.parse(getString(R.string.db_URL)))
        val time = System.currentTimeMillis()
        Log.d(LOG_TAG, "time = $time")
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("${CattlelogDatabase.DATABASE_NAME}.db")
        request.setDescription("CattleLog database is downloading.")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"cattlelogdb_$time.db")

        val manager =  getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        val dbpath = Environment.getExternalStorageDirectory().toString()+ File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "cattlelogdb_$time.db"
        Log.d(LOG_TAG, "dbpath: $dbpath")

        Log.d(LOG_TAG, "Entering installDatabaseFromDownloads")
        // Copies database file into CattleLog's internal data
        installDatabaseFromDownloads(dbpath)

        // Sample database setup and query
        val dbFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")
        Log.d(LOG_TAG, "dstFile: $dbFile")

        AsyncTask.execute {
            Log.d(LOG_TAG, "" + CattlelogDatabase.getDatabase(applicationContext, dbFile).cattleDao().getAllCattle().size)
        }
    }

    private fun installDatabaseFromDownloads(srcPath: String) {
        Log.d(LOG_TAG, "We have not broken and dbPath: $srcPath")
        val source = File(srcPath)

        val startTime = System.currentTimeMillis()
        while (!source.exists() && System.currentTimeMillis()-startTime < 60000){
            Log.d(LOG_TAG, "Waiting on file to download.")
            Thread.sleep(1000)
        }
        // Ensure that the file is completely ready to be copied
        // TODO: set up an alert from DownloadManager instead of a thread.sleep.
        Thread.sleep(3000)

        // Copy the downloaded file to CattleLog's internal storage.
        if (source.exists()){
            val destination = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")
            Log.d(LOG_TAG, "We have not broken and source: $source")
            Log.d(LOG_TAG, "We have not broken and destination: $destination")

            try {
                File(source.toURI()).copyTo(File(destination.toURI()), overwrite = true)
                Log.d(LOG_TAG, "File has been copied successfully.")
            } catch (e: Exception) {
                Log.d(LOG_TAG, "Error copying file.")
                Log.e(LOG_TAG, "exception", e)
            }

        } else{
            // Handle case where file is not downloaded
            Log.d(LOG_TAG, "Source file doesn't exist.")
        }
    }

    // TODO: Remove unneeded files from the Downloads folder if possible (not important)
    fun removeFile(){
        //val root = Environment.getExternalStorageDirectory().toString()
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File("$root/${CattlelogDatabase.DATABASE_NAME}.db")
        val fdelete = File(file.toURI())
        Log.d(LOG_TAG, "file: $file")
        Log.d(LOG_TAG, "fdelete: $fdelete")
        val fileExists = fdelete.exists()
        Log.d(LOG_TAG, fileExists.toString())
        if (fileExists) {
            Log.d(LOG_TAG, "fileExists")
            if (fdelete.delete()) {
                Log.d(LOG_TAG, "file Deleted :" + file.path)
            } else {
                Log.d(LOG_TAG, "file not Deleted :" + file.path)
            }
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
                }
                else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
