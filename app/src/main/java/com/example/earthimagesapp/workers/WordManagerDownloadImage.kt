package com.example.earthimagesapp.workers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.earthimagesapp.util.KEY_IMAGE_PATH
import com.example.earthimagesapp.util.KEY_IMAGE_URL
import com.example.earthimagesapp.util.URL_SEPARATOR_CHARACTER
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

@HiltWorker
class WordManagerDownloadImage @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters
) :
    Worker(appContext, workerParams) {
    override fun doWork(): Result {

        val appContext = applicationContext

        return try {
            val imageUrl = inputData.getString(KEY_IMAGE_URL)
            val filePath = if (imageUrl != null) downloadImage(imageUrl) else ""
            val outputData = workDataOf(KEY_IMAGE_PATH to filePath)

            createForegroundInfo("Image downloading ${imageUrl!!.subSequence(imageUrl!!.length-15, imageUrl!!.length-1)}", appContext)


            Result.success(outputData)
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun downloadImage(imageUrl: String): String {
        try {
            val filename = imageUrl.substringAfterLast(URL_SEPARATOR_CHARACTER)
            if (File(filename).exists()) return ""
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