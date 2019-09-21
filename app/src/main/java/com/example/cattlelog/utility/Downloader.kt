package com.example.cattlelog.utility

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.example.cattlelog.database.CattlelogDatabase

/**
 * Utility class that downloads a file within the given context, using the provided url,
 * and saves it to targetFileName.
 */
class Downloader(private val context: Context, private val url: String) {

    fun downloadAs(targetFileName: String): Long {
        val request = DownloadManager.Request(Uri.parse(url))
        val time = System.currentTimeMillis()
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("${CattlelogDatabase.DATABASE_NAME}.db")
        request.setDescription("CattleLog database is downloading.")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"cattlelogdb_$time.db")

        val manager =  context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return manager.enqueue(request)
    }
}