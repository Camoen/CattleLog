package com.example.cattlelog

import android.content.pm.PackageManager
import android.Manifest
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.TextView
import com.example.cattlelog.database.CattlelogDatabase
import java.io.*

private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "MainActivity"
const val TARGET_FILE_KEY = "DESIRED FILE NAME"

class MainActivity : AppCompatActivity() {

    private lateinit var downloadFileIntent: Intent
    private lateinit var databaseStatusTextView: TextView
    private lateinit var targetDatabaseFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseStatusTextView = findViewById<TextView>(R.id.databaseStatusTextView)
        targetDatabaseFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")
        updateDatabaseAvailabilityStatus()

        downloadButton.setOnClickListener {
            downloadFileIntent = Intent(this@MainActivity, DownloadDatabase::class.java)
            downloadFileIntent.putExtra(TARGET_FILE_KEY, targetDatabaseFile)
            startIntentWithPermission(it, downloadFileIntent)
        }

        // Testing button, logs query results.
        testsqlquery.setOnClickListener {
            AsyncTask.execute{
                Log.d(
                    LOG_TAG,
                    "Test Query: " + CattlelogDatabase.getDatabase(applicationContext).cattleDao().getCattleWithTagNumber(888888)
                )
                Log.d(
                    LOG_TAG,
                    "Test Query 2: " + CattlelogDatabase.getDatabase(applicationContext).cattleDao().getNextExpectedHeatsTEST()
                )
                // Note that preset will probably not yield any results currently (need updated input files)
                Log.d(
                    LOG_TAG,
                    "Test Query 3: " + CattlelogDatabase.getDatabase(applicationContext).cattleDao().getNextExpectedHeatsPreset()
                )
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//        updateDatabaseAvailabilityStatus()
//
//        // TODO replace later, this is only here for rough testing
//        AsyncTask.execute {
//            Log.d(
//                LOG_TAG,
//                "Test Query: " + CattlelogDatabase.getDatabase(applicationContext).cattleDao().getCattleWithTagNumber(888888)
//            )
//            Log.d(
//                LOG_TAG,
//                "Test Query 2: " + CattlelogDatabase.getDatabase(applicationContext).cattleDao().getNextExpectedHeats()
//            )
//        }
//    }

    private fun updateDatabaseAvailabilityStatus() {
        if (targetDatabaseFile.exists()) {
            databaseStatusTextView.setText(getString(R.string.already_have_database))
        } else {
            databaseStatusTextView.setText(getString(R.string.dont_have_database_yet))
        }
    }

    private fun startIntentWithPermission(view: View, intent: Intent) {
        // Checks if version of Android is >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                // request permission if denied
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_CODE)
                // requestPermissions(arrayOf(Manifest.permission_group.STORAGE), PERMISSION_CODE)
            } else {
                // If user allows apps to write to external storage (i.e., we don't need explicit permission)
                startActivity(intent)
            }
        } else {
            // OS is out of date, no permissions are needed
            startActivity(intent)
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
                    startActivity(downloadFileIntent)
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
