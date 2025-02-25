package com.example.notes

import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.notes.data.workers.FirebaseStorageDownloadWorker
import com.example.notes.data.workers.FirebaseStorageUploadWorker
import com.example.notes.screens.util.Constants.DIRECTORY_NAME
import com.example.notes.screens.util.Constants.FILE_NAME
import com.example.notes.screens.util.Constants.FILE_URI
import com.example.notes.screens.util.Constants.MIME_TYPE
import com.example.notes.screens.util.Constants.TABLE_NAME
import java.util.concurrent.TimeUnit

class WorkManagerRepository(private val workManager: WorkManager) {
    fun upload(
        fileUri: String,
        fileName: String?,
        mimeType: String?,
        tableName: String,
    ) {
        val workRequest =
            OneTimeWorkRequestBuilder<FirebaseStorageUploadWorker>()
                .setInputData(
                    workDataOf(
                        FILE_URI to fileUri,
                        FILE_NAME to fileName,
                        MIME_TYPE to mimeType,
                        TABLE_NAME to tableName,
                    ),
                )
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 30, TimeUnit.SECONDS) // Retry if failed
                .build()

        workManager.enqueue(workRequest)
    }

    fun download(
        fileName: String?,
        tableName: String,
        directoryName: String,
    ) {
        val workRequest =
            OneTimeWorkRequestBuilder<FirebaseStorageDownloadWorker>()
                .setInputData(
                    workDataOf(
                        FILE_NAME to fileName,
                        TABLE_NAME to tableName,
                        DIRECTORY_NAME to directoryName,
                    ),
                )
                .build()
        workManager.enqueue(workRequest)
    }
}
