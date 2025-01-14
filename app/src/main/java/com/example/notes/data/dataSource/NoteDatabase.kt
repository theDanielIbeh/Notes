package com.example.notes.data.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase: RoomDatabase() {
    abstract val noteDao: NoteDao
}