package com.example.notes.domain.model.service.impl

import android.util.Log
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteTableConstants
import com.example.notes.domain.model.service.StorageService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class StorageServiceImpl(
    reference: DatabaseReference,
    auth: FirebaseAuth
) : StorageService {
    private val reference = reference.child(auth.currentUser!!.uid)
    override val notes: Flow<List<Note>>
        get() = flow {
            try {
                val snapshot = reference.get().await()
                val result = snapshot.children.mapNotNull { it.toNoteOrNull() }
                emit(result)
            } catch (e: FirebaseException) {
                Log.e("firebase", "Error getting data", e)
                emit(emptyList())
            }
        }

    // Extension function for safe DataSnapshot to Note conversion
    private fun DataSnapshot.toNoteOrNull(): Note? {
        return try {
            Note(
                id = child(NoteTableConstants.ID).value?.toString()?.toIntOrNull() ?: return null,
                title = child(NoteTableConstants.TITLE).value?.toString() ?: return null,
                content = child(NoteTableConstants.CONTENT).value?.toString() ?: return null,
                timeStamp = child(NoteTableConstants.TIME_STAMP).value?.toString()?.toLongOrNull()
                    ?: return null,
                deleteFlag = child(NoteTableConstants.DELETE_FLAG).value?.toString()?.toIntOrNull()
                    ?: return null
            )
        } catch (e: Exception) {
            Log.e("firebase", "Error parsing note", e)
            null
        }
    }

    override suspend fun createNote(note: Note) {
        val noteData = hashMapOf(
            NoteTableConstants.ID to note.id,
            NoteTableConstants.TITLE to note.title,
            NoteTableConstants.CONTENT to note.content,
            NoteTableConstants.TIME_STAMP to note.timeStamp,
            NoteTableConstants.DELETE_FLAG to note.deleteFlag
        )

        reference.get().addOnSuccessListener { snapshot ->
            val enote = snapshot.children.filter {
                it.child(NoteTableConstants.ID).value.toString().toInt() == note.id
            }
            if (enote.isEmpty()) reference.push().setValue(noteData)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    override suspend fun readNote(noteId: String): Note? {
        return try {
            val snapshot = reference.child(noteId).get().await()
            snapshot.toNoteOrNull()
        } catch (e: Exception) {
            Log.e("firebase", "Error getting data", e)
            null
        }
    }

    override suspend fun updateNote(note: Note) {
        reference.get().addOnSuccessListener { snapshot ->
            val noteFromStorage = snapshot.children.filter {
                it.child(NoteTableConstants.ID).value.toString().toInt() == note.id
            }
            reference.child(noteFromStorage[0].key.toString()).setValue(note)
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

    override suspend fun deleteNote(noteId: String) {
        reference.get().addOnSuccessListener { snapshot ->
            val note = snapshot.children.filter {
                it.child(NoteTableConstants.ID).value.toString() == noteId
            }
            reference.child(note[0].key.toString()).removeValue()
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
    }

}