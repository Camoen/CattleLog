package com.example.cattlelog

import android.content.pm.PackageManager
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cattlelog.adapter.CattleListAdapter
import com.example.cattlelog.view.CattleViewModel
import com.example.cattlelog.model.database.CattlelogDatabase
import java.io.*
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import java.security.AccessController.getContext
import org.json.JSONObject
import org.json.JSONException
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.TextView
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import kotlinx.android.synthetic.main.activity_download_database.*
import java.net.HttpURLConnection
import java.net.URL


import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.cfsuman.jetpack.VolleySingleton
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Method
import kotlin.reflect.jvm.internal.impl.resolve.constants.NullValue


private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "MainActivity"
const val TARGET_FILE_KEY = "DESIRED FILE NAME"
const val DB_DOWNLOAD_CODE = 50
const val PREFS_FILENAME = "com.example.cattlelog.shared_preferences"
const val DB_VERSION = "database_version"

class MainActivity : AppCompatActivity() {

    private lateinit var downloadFileIntent: Intent
    private lateinit var databaseStatusTextView: TextView
    private lateinit var targetDatabaseFile: File
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var viewAdapter: RecyclerView.Adapter<*>
//    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var cattleViewModel: CattleViewModel
    private lateinit var cattleRecyclerView: RecyclerView
    private lateinit var cattleAdapter: CattleListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        targetDatabaseFile = File(filesDir, "${CattlelogDatabase.DATABASE_NAME}.db")

        downloadButton.setOnClickListener {download()}
        testsqlquery.setOnClickListener {testQuery()}

        cattleRecyclerView = findViewById(R.id.herdList)
        cattleAdapter = CattleListAdapter(this)
        cattleRecyclerView.setHasFixedSize(true)
        cattleRecyclerView.adapter = cattleAdapter
        cattleRecyclerView.layoutManager = LinearLayoutManager(this)
        val divider = DividerItemDecoration(cattleRecyclerView.context,DividerItemDecoration.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.divider) as Drawable)
        cattleRecyclerView.addItemDecoration(divider)

        cattleViewModel = ViewModelProvider(this).get(CattleViewModel::class.java)
        cattleViewModel.allCattle.observe(this, Observer { cattleList ->
            cattleList?.let { cattleAdapter.setCattleList(it) }
        })
    }

    // TODO get rid of this later, we shouldn't need it
    private fun testQuery() {
        AsyncTask.execute {
            Log.d(
                LOG_TAG,
                "Test Query: " + CattlelogDatabase.getDatabase(applicationContext).cattleDao().getCattleWithTagNumber(
                    888888
                )
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

//        val instDownloadDatabase = DownloadDatabase()
//        DownloadDatabase::getDatabaseVersion.call(instDownloadDatabase, true)
        getDatabaseVersion(false)
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
                    startActivityForResult(downloadFileIntent, DB_DOWNLOAD_CODE)
                } else {
                    Toast.makeText(this, "Permission denied.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    /**
     * Builds the menu for this activity. See res/menu/cattlelog_menu.xml for all Items that this menu renders.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.cattlelog_menu, menu)

        val searchItem: MenuItem? = menu?.findItem(R.id.action_search)
        val searchView: SearchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // Whenever the user types something in the search bar, we apply the filter.
            // See CattleListAdapter.kt's cattleFilter for how the filtering is actually done.
            override fun onQueryTextChange(newText: String): Boolean {
                cattleAdapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }
        })

        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("onActResult", "reqCode: $requestCode,  resultCode: $resultCode, data: $data")
        if (requestCode == DB_DOWNLOAD_CODE){
            if (resultCode == Activity.RESULT_OK){
                getDatabaseVersion(true)
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

    // Sets database version after database is successfully downloaded (call it with getDatabaseVersion(true) for this functionality)
    // Triggers new database download if local database is out of date (call it with getDatabaseVersion(false) for this functionality)
    fun getDatabaseVersion(download_finished: Boolean) {
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
