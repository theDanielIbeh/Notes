package com.example.notes.domain.model.service.impl

import android.util.Log
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteTableConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

fun noteStorageService(
    firebaseDatabaseReference: DatabaseReference,
    firebaseAuth: FirebaseAuth
) = DatabaseServiceImpl(
    reference = firebaseDatabaseReference,
    auth = firebaseAuth,
    tableName = NoteTableConstants.NOTE_TABLE,
    fromSnapshot = { snapshot ->
        try {
            Note(
                id = snapshot.child(NoteTableConstants.ID).value?.toString()?.toIntOrNull(),
                title = snapshot.child(NoteTableConstants.TITLE).value?.toString() ?: "",
                content = snapshot.child(NoteTableConstants.CONTENT).value?.toString() ?: "",
                deleteFlag = snapshot.child(NoteTableConstants.DELETE_FLAG).value?.toString()?.toIntOrNull() ?: 0,
                timeStamp = snapshot.child(NoteTableConstants.TIME_STAMP).value?.toString()?.toLongOrNull() ?: System.currentTimeMillis()
            )
        } catch (e: Exception) {
            Log.e("firebase", "Error parsing note", e)
            null
        }
    },
    toMap = { note ->
        mapOf(
            NoteTableConstants.ID to note.id,
            NoteTableConstants.TITLE to note.title,
            NoteTableConstants.CONTENT to note.content,
            NoteTableConstants.DELETE_FLAG to note.deleteFlag,
            NoteTableConstants.TIME_STAMP to note.timeStamp
        )
    }
)
