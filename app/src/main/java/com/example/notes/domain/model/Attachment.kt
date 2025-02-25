package com.example.notes.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = AttachmentTableConstants.ATTACHMENT_TABLE,
    foreignKeys = [
        ForeignKey(
            entity = Note::class,
            parentColumns = [NoteTableConstants.ID],
            childColumns = [AttachmentTableConstants.NOTE_ID],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = [AttachmentTableConstants.NOTE_ID])],
)
data class Attachment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AttachmentTableConstants.ID)
    val id: Int? = null,
    @ColumnInfo(name = AttachmentTableConstants.NOTE_ID)
    val noteId: Int,
    @ColumnInfo(name = AttachmentTableConstants.URI)
    val uri: String,
    @ColumnInfo(name = AttachmentTableConstants.TIME_STAMP)
    val timeStamp: Long = System.currentTimeMillis(),
)

object AttachmentTableConstants {
    const val ATTACHMENT_TABLE = "attachment"

    const val ID = "id"
    const val NOTE_ID = "note_id"
    const val URI = "uri"
    const val TIME_STAMP = "time_stamp"
}
