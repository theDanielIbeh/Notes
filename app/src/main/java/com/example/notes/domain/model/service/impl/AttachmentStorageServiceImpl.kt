package com.example.notes.domain.model.service.impl

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import com.example.notes.WorkManagerRepository
import com.example.notes.domain.model.AttachmentTableConstants.ATTACHMENT_TABLE
import com.example.notes.screens.util.FileUtils.DOCUMENTS
import com.example.notes.screens.util.FileUtils.PICTURES
import com.example.notes.screens.util.FileUtils.createFileFromUri
import com.example.notes.screens.util.FileUtils.createFilesDirectory
import com.example.notes.screens.util.FileUtils.isImageFile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storageMetadata

class AttachmentStorageServiceImpl(
    private val application: Application,
    reference: StorageReference,
    auth: FirebaseAuth,
    private val workManagerRepository: WorkManagerRepository
) {

    private val reference = reference.child(ATTACHMENT_TABLE).child(auth.currentUser!!.uid)

    fun uploadFile(fileUri: String, fileName: String?, mimeType: String?) {
//        try {
//            val fileRef = reference.child(fileName)
//            fileRef.putFile(fileUri.toUri()).await()
//        } catch (e: Exception) {
//            Log.e("firebase", "Error uploading file", e)
//            throw e
//        }
//        val uri = fileUri.toUri()
//        val fileRef = reference.child(fileName)
//        val metadata = storageMetadata {
//            contentType = mimeType
//        }
//        val uploadTask = fileRef.putFile(uri, metadata)
//
//        uploadTask.addOnSuccessListener {
//            Log.d("firebase", "File uploaded successfully")
//        }.addOnFailureListener {
//            Log.e("firebase", "Error uploading file", it)
//        }
        workManagerRepository.upload(fileUri, fileName, mimeType, ATTACHMENT_TABLE)
    }

    fun downloadFile(fileName: String, directoryName: String) {
//        val fileRef = reference.child(fileName)
//
//        fileRef.downloadUrl.addOnSuccessListener { uri ->
//            Log.d("firebase", "File downloaded successfully: $uri")
//            if (isImageFile(context = application, uri = uri)) {
//                createFileFromUri(
//                    context = application,
//                    directoryName = createFilesDirectory(application, PICTURES),
//                    uri = uri
//                )
//            } else {
//                createFileFromUri(
//                    context = application,
//                    directoryName = createFilesDirectory(application, DOCUMENTS),
//                    uri = uri
//                )
//            }
//        }.addOnFailureListener {
//            Log.e("firebase", "Error downloading file", it)
//        }
        workManagerRepository.download(fileName, ATTACHMENT_TABLE, directoryName)
    }

    fun deleteFile(fileName: String) {
        val fileRef = reference.child(fileName)
        fileRef.delete().addOnSuccessListener {
            Log.d("firebase", "File deleted successfully")
        }.addOnFailureListener {
            Log.e("firebase", "Error deleting file", it)
        }
    }
}
