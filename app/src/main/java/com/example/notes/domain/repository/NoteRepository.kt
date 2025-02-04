package com.example.notes.domain.repository

import com.example.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>
    fun getActiveNotes(): Flow<List<Note>>
    fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun getNoteByTimeStamp(timeStamp: Long): Note?
    suspend fun insertNote(note: Note)
    suspend fun deleteNoteById(id: Int)
}