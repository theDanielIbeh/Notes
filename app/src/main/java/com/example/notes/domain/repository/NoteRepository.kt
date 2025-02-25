package com.example.notes.domain.repository

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteWithAttachments
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<NoteWithAttachments>>

    fun getActiveNotes(): Flow<List<NoteWithAttachments>>

    fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<NoteWithAttachments>>

    suspend fun getNoteById(id: Int): NoteWithAttachments?

    suspend fun getNoteByTimeStamp(timeStamp: Long): NoteWithAttachments?

    suspend fun insertNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNoteById(id: Int)
}
