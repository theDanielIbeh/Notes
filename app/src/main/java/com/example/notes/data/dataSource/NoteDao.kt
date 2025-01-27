package com.example.notes.data.dataSource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteTableConstants
import com.example.notes.domain.model.NoteTableConstants.NOTE_TABLE
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM $NOTE_TABLE")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE ${NoteTableConstants.ID} = :id")
    suspend fun getNoteById(id: Int): Note?

    @Query("SELECT * FROM $NOTE_TABLE WHERE ${NoteTableConstants.DELETE_FLAG} = 0 ORDER BY ${NoteTableConstants.TIME_STAMP} DESC")
    fun getActiveNotes(): Flow<List<Note>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE ${NoteTableConstants.DELETE_FLAG} = 1 and ${NoteTableConstants.TIME_STAMP} > :timestamp")
    fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<Note>>

    @Query("SELECT * FROM $NOTE_TABLE WHERE ${NoteTableConstants.TIME_STAMP} = :timeStamp")
    suspend fun getNoteByTimeStamp(timeStamp: Long): Note?

    @Query("DELETE FROM $NOTE_TABLE WHERE ${NoteTableConstants.ID} = :id")
    suspend fun deleteNoteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)
}