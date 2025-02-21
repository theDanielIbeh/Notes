package com.example.notes.data.dataSource.note

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteTableConstants
import com.example.notes.domain.model.NoteWithAttachments
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Transaction
    @Query("SELECT * FROM ${NoteTableConstants.NOTE_TABLE}")
    fun getNotes(): Flow<List<NoteWithAttachments>>

    @Transaction
    @Query("SELECT * FROM ${NoteTableConstants.NOTE_TABLE} WHERE ${NoteTableConstants.ID} = :id")
    suspend fun getNoteById(id: Int): NoteWithAttachments?

    @Transaction
    @Query("SELECT * FROM ${NoteTableConstants.NOTE_TABLE} WHERE ${NoteTableConstants.DELETE_FLAG} = 0 ORDER BY ${NoteTableConstants.TIME_STAMP} DESC")
    fun getActiveNotes(): Flow<List<NoteWithAttachments>>

    @Transaction
    @Query("SELECT * FROM ${NoteTableConstants.NOTE_TABLE} WHERE ${NoteTableConstants.DELETE_FLAG} = 1 and ${NoteTableConstants.TIME_STAMP} > :timestamp")
    fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<NoteWithAttachments>>

    @Transaction
    @Query("SELECT * FROM ${NoteTableConstants.NOTE_TABLE} WHERE ${NoteTableConstants.TIME_STAMP} = :timeStamp")
    suspend fun getNoteByTimeStamp(timeStamp: Long): NoteWithAttachments?

    @Query("DELETE FROM ${NoteTableConstants.NOTE_TABLE} WHERE ${NoteTableConstants.ID} = :id")
    suspend fun deleteNoteById(id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateNote(note: Note)
}