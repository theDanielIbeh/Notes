package com.example.notes.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = NoteTableConstants.NOTE_TABLE)
data class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = NoteTableConstants.ID)
    val id: Int? = null,
    @ColumnInfo(name = NoteTableConstants.TITLE)
    val title: String = "",
    @ColumnInfo(name = NoteTableConstants.CONTENT)
    val content: String = "",
    @ColumnInfo(name = NoteTableConstants.DELETE_FLAG)
    val deleteFlag: Int = 0,
    @ColumnInfo(name = NoteTableConstants.TIME_STAMP)
    val timeStamp: Long = System.currentTimeMillis(),
)

class InvalidNoteException(message: String) : Exception(message)

object NoteTableConstants {
    const val NOTE_TABLE = "note"

    const val ID = "id"
    const val TITLE = "title"
    const val CONTENT = "content"
    const val DELETE_FLAG = "delete_flag"
    const val TIME_STAMP = "time_stamp"
}
