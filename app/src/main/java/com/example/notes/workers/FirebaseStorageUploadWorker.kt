package com.example.notes.workers

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.notes.screens.util.Constants.FILE_NAME
import com.example.notes.screens.util.Constants.FILE_URI
import com.example.notes.screens.util.Constants.MIME_TYPE
import com.example.notes.screens.util.Constants.TABLE_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.tasks.await

class FirebaseStorageUploadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val fileUri = inputData.getString(FILE_URI)?.toUri() ?: return Result.failure()
        val fileName = inputData.getString(FILE_NAME) ?: return Result.failure()
        val mimeType = inputData.getString(MIME_TYPE) ?: return Result.failure()
        val tableName = inputData.getString(TABLE_NAME) ?: return Result.failure()
        return try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return Result.failure()
            val reference = FirebaseStorage.getInstance().reference
                .child(tableName)
                .child(userId)
                .child(fileName)

            val metadata = storageMetadata { contentType = mimeType }
            reference.putFile(fileUri, metadata).await()

            Log.d("UploadWorker", "File uploaded successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("UploadWorker", "Error uploading file", e)
//            Result.retry()  // Retry upload if it fails
            Result.failure()
        }
    }
}