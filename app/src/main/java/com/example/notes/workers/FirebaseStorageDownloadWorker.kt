package com.example.notes.workers

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.notes.screens.util.Constants.DIRECTORY_NAME
import com.example.notes.screens.util.Constants.FILE_NAME
import com.example.notes.screens.util.Constants.TABLE_NAME
import com.example.notes.screens.util.FileUtils.DOCUMENTS
import com.example.notes.screens.util.FileUtils.PICTURES
import com.example.notes.screens.util.FileUtils.createFilesDirectory
import com.example.notes.screens.util.FileUtils.isImageFile
import com.example.notes.screens.util.FileUtils.saveFileToStorage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseStorageDownloadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val fileName = inputData.getString(FILE_NAME) ?: return Result.failure()
        val tableName = inputData.getString(TABLE_NAME) ?: return Result.failure()
        val directoryName = inputData.getString(DIRECTORY_NAME) ?: return Result.failure()

        return try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return Result.failure()
            val reference = FirebaseStorage.getInstance().reference
                .child(tableName)
                .child(userId)
                .child(fileName)

            // Step 1: Get the Download URL
            val fileUri = reference.downloadUrl.await().toString()
            Log.d("DownloadWorker", "File URL: $fileUri")

            // Step 2: Save the File Locally
            val savedFilePath = saveFileToStorage(
                fileUri,
                fileName,
                directoryName = directoryName
            )


            // Step 3: Return success with file path
            if (savedFilePath != null) {
                Result.success()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Log.e("DownloadWorker", "Error downloading file", e)
//            Result.retry()  // Retry download if it fails
            Result.failure()
        }
    }
}