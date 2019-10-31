package com.example.cattlelog.main_activity

import android.content.pm.PackageManager
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cattlelog.model.database.CattlelogDatabase
import java.io.*
import org.json.JSONObject
import androidx.viewpager.widget.ViewPager


import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.cfsuman.jetpack.VolleySingleton
import com.example.cattlelog.DownloadDatabase
import com.example.cattlelog.R
import com.google.android.material.tabs.TabLayout


private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "MainActivity"
const val TARGET_FILE_KEY = "DESIRED FILE NAME"
const val DB_DOWNLOAD_CODE = 50
const val PREFS_FILENAME = "com.example.cattlelog.shared_preferences"
const val DB_VERSION = "database_version"

class MainActivity : AppCompatActivity() {

    private lateinit var downloadFileIntent: Intent
    private lateinit var targetDatabaseFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetDatabaseFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")

        val sectionsPagerAdapter =
            TabPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        // TODO: May want to run setDatabaseVersion here.  Not sure where the best place to call it is, yet!
        //setDatabaseVersion(false)
    }

    private fun download() {
        downloadFileIntent = Intent(this@MainActivity, DownloadDatabase::class.java)
        downloadFileIntent.putExtra(TARGET_FILE_KEY, targetDatabaseFile)
        startIntentWithPermission(downloadFileIntent)
    }

    private fun startIntentWithPermission(intent: Intent) {
        // Checks if version of Android is >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                // request permission if denied
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
                // requestPermissions(arrayOf(Manifest.permission_group.STORAGE), PERMISSION_CODE)
            } else {
                // If user allows apps to write to external storage (i.e., we don't need explicit permission)
                startActivityForResult(intent, DB_DOWNLOAD_CODE)
            }
        } else {
            // OS is out of date, no permissions are needed
            startActivityForResult(intent, DB_DOWNLOAD_CODE)
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
                    startActivityForResult(downloadFileIntent,
                        DB_DOWNLOAD_CODE
                    )
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActResult", "reqCode: $requestCode,  resultCode: $resultCode, data: $data")
        if (requestCode == DB_DOWNLOAD_CODE){
            if (resultCode == Activity.RESULT_OK){
                setDatabaseVersion(true)
            }
        }
    }

    fun setDatabaseVersion(newestDBVersion: String){
        val prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putString(DB_VERSION, newestDBVersion)
        editor.apply()
        val localVersion = prefs.getString(DB_VERSION, "NONE")
        Log.d("dropboxAPI", "The stored database version has been set to $localVersion")
    }

    fun checkDatabaseCurrent(newestDBVersion: String): Boolean{
        val prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val localVersion = prefs!!.getString(DB_VERSION, "NONE")
        if (localVersion == newestDBVersion){
            return true
        }
        return false
    }

    // Sets database version after database is successfully downloaded (call it with setDatabaseVersion(true) for this functionality)
    // Triggers new database download if local database is out of date (call it with setDatabaseVersion(false) for this functionality)
    fun setDatabaseVersion(download_finished: Boolean) {
        var newestDBVersion = ""
        Log.d("dropboxAPI", "entered check file function.")
        Log.d("dropboxAPI", resources.getString(R.string.CattleLog_DropboxAPIKey))

        val url = "https://api.dropboxapi.com/2/files/get_metadata"
        val params = HashMap<String,String>()
        params["path"] = "/${CattlelogDatabase.DATABASE_NAME}.db"
        val jsonObject = JSONObject(params)

        // Some help from https://android--code.blogspot.com/2019/02/android-kotlin-volley-post-request-with.html
        // Volley post request with parameters
        val request = object: JsonObjectRequest(
            Method.POST,url,jsonObject,
            Response.Listener { response ->
                // Process the json
                try {
                    Log.d("dropboxAPI", "Response: $response")
                    newestDBVersion = response["server_modified"].toString()
                    Log.d("dropboxAPI", "Last Modification of CattleLog DB: $newestDBVersion")
                    if (download_finished){
                        // The most recent version of the database has been downloaded
                        setDatabaseVersion(newestDBVersion)
                    } else {
                        // Check that the local copy of the database is up to date
                        if (!checkDatabaseCurrent(newestDBVersion)){
                            // If local database version is out of date, download new version
                            Log.d("dropboxAPI", "Local database is out of date, get new version.")
                            download()

                        } else {
                            Log.d("dropboxAPI", "Local database is up to date.")
                        }
                    }
                }catch (e:Exception){
                    Log.d("dropboxAPI", "Exception: $e")
                }

            }, Response.ErrorListener{
                // Error in request
                Log.d("dropboxAPI", "Volley error: $it")
            }){

            override fun getHeaders(): HashMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Authorization"] = "Bearer " + resources.getString(R.string.CattleLog_DropboxAPIKey)
                return headers
            }

        }

        // Volley request policy, only one time request to avoid duplicate transaction
        request.retryPolicy = DefaultRetryPolicy(
            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
            // Use 0 to prevent retries
            0,
            1f
        )

        // Add the volley post request to the request queue
        VolleySingleton.getInstance(this).addToRequestQueue(request)
    }
}
