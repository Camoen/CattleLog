package com.example.cattlelog

import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.Manifest
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import java.io.*


private const val PERMISSION_CODE = 1000


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

    private fun internalDownload(): Boolean {
        return try {

            true
        } catch (e: Exception) {

            false
        }

    }


    private fun startDownload() {
        val request = DownloadManager.Request(Uri.parse(getString(R.string.db_URL)))
        val time = System.currentTimeMillis()
        Log.d("outputs", "time = $time")
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("cattlelog_database.db")
        request.setDescription("CattleLog database is downloading.")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "${System.currentTimeMillis()}") // Working
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"cattlelogdb_$time.db")
        //request.setDestinationInExternalFilesDir()

        val manager =  getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)

//        val srcFile = File(Environment.getExternalStorageDirectory(), "cattlelog_database.db")
//        val dstFile = File(this.filesDir.toString() + "/cattlelog_database.db")

        //val instrumentFileList = this.fileList()
//        Log.d("outputs", "filesDir: $filesDir")
//        val instrumentFileList = filesDir.list()
//
//        Log.d("outputs", "File List Internal: ")
//        for (i in instrumentFileList){
//            Log.d("outputs", i.toString())
//        }
//
//        Log.d("outputs", "File List External: ")
//        for (i in getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).listFiles()){
//            Log.d("outputs", i.toString())
//        }
//
//        Log.d("outputs", "File List External 2: ")
//        for (i in Environment.getExternalStorageDirectory().listFiles()){
//            Log.d("outputs", i.toString())
//        }

        //getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

//        val path = Environment.getExternalStorageDirectory().toString()+ File.separator + Environment.DIRECTORY_DOWNLOADS
//        val filepath = File(path)
//        val mRootPath = filepath.absoluteFile.path;
//        Log.d("outputs", "filepath: $filepath")
//        Log.d("outputs","rootpath: $mRootPath")
//        Log.d("outputs", "File List External 3: ")
//        for (i in filepath.listFiles()){
//            Log.d("outputs", i.toString())
//        }

        val dbpath = Environment.getExternalStorageDirectory().toString()+ File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + "cattlelogdb_$time.db"
        Log.d("outputs", "dbpath: $dbpath")
        Log.d("outputs", "Entering installDatabaseFromDownloads")
        installDatabaseFromDownloads(dbpath)


        val dbFile = File(filesDir, "cattlelog_database.db")
        Log.d("outputs", "dstFile: $dbFile")

        val db = SQLiteDatabase.openOrCreateDatabase(dbFile, null)
        //val db = SQLiteDatabase.openOrCreateDatabase("/storage/emulated/0/Download/cattlelogdb_1568318165945.db", null)
        Log.d("outputs", db.toString())


        var c: Cursor
        /* get cursor on it */
        try
        {
            c = db.query("cattle", null,null, null, null, null, null);
            Log.d("outputs"+"cursor", "cattle exists! :)))")
            c = db.query("treatment", null,null, null, null, null, null);
            Log.d("outputs"+"cursor", "treatment exists! :)))")
            c = db.query("health", null,null, null, null, null, null);
            Log.d("outputs"+"cursor", "health exists! :)))")
            c = db.query("userFields", null,null, null, null, null, null);
            Log.d("outputs"+"cursor", "userFields exists! :)))")
            // set limit to 5 to dump first 5 records
            c = db.query("cattle", null,null, null, null, null, null, "5");
//            Log.v("outputs"+"cursor", DatabaseUtils.dumpCursorToString(c))
//            while (c.moveToNext()) {
//                Log.d("outputs", c.toString())
//            }
            c.close()
        }
        catch (e: java.lang.Exception) {
            /* fail */
            Log.e("outputs", "exception", e)
            Log.d("outputs"+"cursor", "cattle doesn't exist :(((")
        }

        Log.d("outputs", "Time to check if this database has data")
        val tableName = "cattle"
        val columnNames: Array<String> = arrayOf("TagNumber", "BirthDate", "FarmID")
        val whereClause = "TagNumber < ?"
        val whereArgs: Array<String> = arrayOf("750")
        val groupBy = null
        val having = null
        val orderBy = null
        val limit = "5"

        Log.d("outputs", "check if cursor crashes here")
        try {
            val c1: Cursor = db.query(tableName, columnNames, whereClause, whereArgs, groupBy, having, orderBy)
            Log.v("outputs" + "cursor", DatabaseUtils.dumpCursorToString(c1))
            c1.close()
        }
        catch (e: java.lang.Exception) {
            /* fail */
            Log.e("outputs", "exception", e)
            Log.d("outputs"+"cursor", "why does cursor break this send help")
        }
        Log.d("outputs", "oof ouch we made it")



//        try {
//            while (c1.moveToNext()) {
//                Log.d("outputs", c1.toString())
//            }
//        } finally {
//            c1.close()
//        }


//        Log.d("outputs", "Entering copyFile")
//        copyFile(srcFile, dstFile)
    }


    private fun installDatabaseFromDownloads(srcPath: String) {
        Log.d("outputs", "We have not broken and dbPath: $srcPath")

        //val toDB_PATH2 = "/data/data/" + this.packageName + "/files/"
        //Log.d("outputs", "We have not broken and toDB_PATH2: $toDB_PATH2")

        //val source = File("/storage/emulated/0/Download/cattlelogdb_1568324605776.db")
        val source = File(srcPath)

        val startTime = System.currentTimeMillis()
        while (!source.exists() && System.currentTimeMillis()-startTime < 60000){
            Log.d("outputs", "waiting on file to download")
            Thread.sleep(1000)
        }
        Thread.sleep(3000)

        if (source.exists()){
            val destination = File(filesDir, "cattlelog_database.db")
            Log.d("outputs", "We have not broken and source: $source")
            Log.d("outputs", "We have not broken and destination: $destination")

            try {
                File(source.toURI()).copyTo(File(destination.toURI()), overwrite = true)
                Log.d("outputs", "We have not broken")
            } catch (e: Exception) {
                Log.d("outputs", "We have certainly broken")
                Log.e("outputs", "exception", e)
            }

            Log.d("outputs", "We may continue")
        } else{
            // Handle case where file is not downloaded
        }


    }

    // I'd like to remove unneeded files from the Downloads folder if possible (not important)
    fun removeFile(){
        //val root = Environment.getExternalStorageDirectory().toString()
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File("$root/cattlelog_database.db")
        val fdelete = File(file.toURI())
        Log.d("outputs", "file: $file")
        Log.d("outputs", "fdelete: $fdelete")
        val fileExists = fdelete.exists()
        Log.d("outputs", fileExists.toString())
        if (fileExists) {
            Log.d("outputs", "fileExists")
            if (fdelete.delete()) {
                Log.d("outputs", "file Deleted :" + file.path)
            } else {
                Log.d("outputs", "file not Deleted :" + file.path)
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
