package com.example.cattlelog

import android.content.pm.PackageManager
import android.Manifest
import android.content.Intent
import android.graphics.drawable.Drawable
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


private const val PERMISSION_CODE = 1000
private const val LOG_TAG = "MainActivity"
const val TARGET_FILE_KEY = "DESIRED FILE NAME"

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

        downloadButton.setOnClickListener {download(it)}
        testsqlquery.setOnClickListener {testQuery(it)}

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
    private fun testQuery(it: View) {
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
        getDatabaseVersion()
    }

    private fun download(it: View) {
        downloadFileIntent = Intent(this@MainActivity, DownloadDatabase::class.java)
        downloadFileIntent.putExtra(TARGET_FILE_KEY, targetDatabaseFile)
        startIntentWithPermission(it, downloadFileIntent)
    }

    private fun startIntentWithPermission(view: View, intent: Intent) {
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

    private fun getDatabaseVersion() {
        var newestDBVersion = ""
        Log.d("dropboxAPI", "entered check file function.")
        Log.d("dropboxAPI", resources.getString(R.string.CattleLog_DropboxAPIKey))

        val url = "https://api.dropboxapi.com/2/files/get_metadata"
        val params = HashMap<String,String>()
        params["path"] = "/cattlelog_database.db"
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
