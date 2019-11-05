package com.example.cattlelog

import android.Manifest
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.cfsuman.jetpack.VolleySingleton
import com.example.cattlelog.main_activity.*
import com.example.cattlelog.model.database.CattlelogDatabase
import com.example.cattlelog.utility.Downloader
import kotlinx.android.synthetic.main.activity_init.*
import org.json.JSONObject
import java.io.File

private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "InitActivity"

class InitActivity : AppCompatActivity() {
    private lateinit var mainActivityIntent: Intent
    private lateinit var databaseDownloader: Downloader
    private lateinit var tempDownloadFile: File
    private lateinit var targetDownloadFile: File
    private var cachedDownloadID: Long = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        targetDownloadFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")
        mainActivityIntent = Intent(this@InitActivity, MainActivity::class.java)

        // Check the database version: Do we have the latest copy of the database file?
        // - If we do, we'll go straight to MainActivity. Nothing else is needed.
        // - If we don't have the latest copy, download it, update the stored version, and go to MainActivity.
        checkDatabaseVersion()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(onDownloadComplete)
    }

    private fun checkDatabaseVersion() {
        var newestDBVersion: String

        val url = "https://api.dropboxapi.com/2/files/get_metadata"
        val params = HashMap<String,String>()
        params["path"] = "/${CattlelogDatabase.DATABASE_NAME}.db"
        val jsonObject = JSONObject(params)

        // Some help from https://android--code.blogspot.com/2019/02/android-kotlin-volley-post-request-with.html
        val request = object: JsonObjectRequest(
            Method.POST,url,jsonObject,
            Response.Listener { response ->
                try {
                    newestDBVersion = response["server_modified"].toString()

                    if (currentDatabaseMatches(newestDBVersion)) {
                        startActivity(mainActivityIntent)
                    } else {
                        downloadLatestDatabaseFile()
                        setDatabaseVersion(newestDBVersion)
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

    private fun currentDatabaseMatches(newestDBVersion: String): Boolean {
        val prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val localVersion = prefs!!.getString(DB_VERSION, "NONE")
        return localVersion == newestDBVersion
    }

    fun setDatabaseVersion(newestDBVersion: String) {
        val prefs = this.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val editor = prefs!!.edit()
        editor.putString(DB_VERSION, newestDBVersion)
        editor.apply()
    }

    private fun downloadLatestDatabaseFile() {
        initText.text = getString(R.string.downloading_data)

        databaseDownloader = Downloader(applicationContext, getString(R.string.db_URL))
        registerReceiver(onDownloadComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        getPermissionToWriteToDeviceStorage()

        tempDownloadFile = File(
            Environment.getExternalStorageDirectory().toString() +
                    File.separator + Environment.DIRECTORY_DOWNLOADS +
                    File.separator + "cattlelogdb_${System.currentTimeMillis()}.db")

        cachedDownloadID = databaseDownloader.download(CattlelogDatabase.DATABASE_NAME, tempDownloadFile)
    }

    private val onDownloadComplete = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val incomingDownloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (incomingDownloadID == cachedDownloadID) {
                Toast.makeText(applicationContext, "Download completed.", Toast.LENGTH_SHORT).show()
                copyToTargetFile()
            }

            startActivity(mainActivityIntent)
        }
    }

    private fun copyToTargetFile() {
        try {
            tempDownloadFile.copyTo(targetDownloadFile, overwrite = true)
            tempDownloadFile.delete()
        } catch (e: Exception) {
            Log.d(LOG_TAG, e.toString())
        }
    }

    private fun getPermissionToWriteToDeviceStorage() {
        // Checks if version of Android is >= Marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // request permission if denied
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_CODE
                )
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
                    downloadLatestDatabaseFile()
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
