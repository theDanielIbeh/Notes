package com.example.notes.domain.model.service.impl

import android.util.Log
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.AttachmentTableConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

fun attachmentStorageService(
    firebaseDatabaseReference: DatabaseReference,
    firebaseAuth: FirebaseAuth
) = DatabaseServiceImpl(
    reference = firebaseDatabaseReference,
    auth = firebaseAuth,
    tableName = AttachmentTableConstants.ATTACHMENT_TABLE,
    fromSnapshot = { snapshot ->
        try {
            Attachment(
                id = snapshot.child(AttachmentTableConstants.ID).value?.toString()?.toIntOrNull(),
                noteId = snapshot.child(AttachmentTableConstants.NOTE_ID).value?.toString()?.toIntOrNull() ?: -1,
                uri = snapshot.child(AttachmentTableConstants.URI).value?.toString() ?: "",
                timeStamp = snapshot.child(AttachmentTableConstants.TIME_STAMP).value?.toString()?.toLongOrNull() ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e("firebase", "Error parsing attachment", e)
            null
        }
    },
    toMap = { attachment ->
        mapOf(
            AttachmentTableConstants.ID to attachment.id,
            AttachmentTableConstants.NOTE_ID to attachment.noteId,
            AttachmentTableConstants.URI to attachment.uri,
            AttachmentTableConstants.TIME_STAMP to attachment.timeStamp
        )
    }
)
