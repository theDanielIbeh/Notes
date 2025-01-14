package com.example.notes.data.repository

import com.example.notes.data.dataSource.NoteDao
import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    override fun getActiveNotes(): Flow<List<Note>> {
        return noteDao.getActiveNotes()
    }

    override fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<Note>> {
        return noteDao.getRecentlyDeletedNotes(timestamp)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }

    override suspend fun deleteNoteById(id: Int) {
        noteDao.deleteNoteById(id)
    }
}