package com.example.notes.data.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.notes.data.dataSource.attachment.AttachmentDao
import com.example.notes.data.dataSource.note.NoteDao
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.Note

@Database(
    entities = [Note::class, Attachment::class],
    version = 1,
    exportSchema = false,
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao
    abstract val attachmentDao: AttachmentDao

    companion object {
        const val DATABASE_NAME = "note_db"
    }
}
