package com.example.notes.domain.model

import androidx.room.Embedded
import androidx.room.Relation

data class NoteWithAttachments(
    @Embedded val note: Note = Note(),
    @Relation(
        parentColumn = NoteTableConstants.ID,
        entityColumn = AttachmentTableConstants.NOTE_ID
    )
    val attachments: List<Attachment>? = null
)