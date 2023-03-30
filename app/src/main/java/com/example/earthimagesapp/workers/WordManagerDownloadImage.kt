package com.example.earthimagesapp.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.earthimagesapp.util.KEY_IMAGE_PATH
import com.example.earthimagesapp.util.KEY_IMAGE_URL
import com.example.earthimagesapp.util.URL_SEPARATOR_CHARACTER
import timber.log.Timber
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class WordManagerDownloadImage(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {
        return try {
            val imageUrl = inputData.getString(KEY_IMAGE_URL)
            val filePath = if (imageUrl != null) downloadImage(imageUrl) else ""
            val outputData = workDataOf(KEY_IMAGE_PATH to filePath)
            Timber.d("Word manager result succcess")
            Result.success(outputData)
        } catch (e: Exception) {
            Timber.d("Word manager result failur")
            Result.failure()
        }
    }

    private fun downloadImage(imageUrl: String): String {
        try {
            val filename = imageUrl.substringAfterLast(URL_SEPARATOR_CHARACTER) + ".png"
            if(File(filename).exists()) return ""
            val connection: HttpURLConnection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.connect()
            if (connection.responseCode != 200) return ""
            BitmapFactory.decodeStream(connection.inputStream).compress(
                Bitmap.CompressFormat.PNG,
                100,
                applicationContext.openFileOutput(
                    filename,
                    Context.MODE_PRIVATE
                )
            )
            connection.disconnect()
            return applicationContext
                .getFileStreamPath(filename)
                .absolutePath
        } catch (e: Exception) {
            return ""
        }
    }
}