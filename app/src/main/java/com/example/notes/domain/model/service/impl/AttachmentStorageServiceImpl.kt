package com.example.notes.domain.model.service.impl

import android.util.Log
import com.example.notes.WorkManagerRepository
import com.example.notes.domain.model.AttachmentTableConstants.ATTACHMENT_TABLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference

class AttachmentStorageServiceImpl(
    reference: StorageReference,
    auth: FirebaseAuth,
    private val workManagerRepository: WorkManagerRepository,
) {
    private val reference = reference.child(ATTACHMENT_TABLE).child(auth.currentUser!!.uid)

    fun uploadFile(
        fileUri: String,
        fileName: String?,
        mimeType: String?,
    ) {
        workManagerRepository.upload(fileUri, fileName, mimeType, ATTACHMENT_TABLE)
    }

    fun downloadFile(
        fileName: String,
        directoryName: String,
    ) {
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
