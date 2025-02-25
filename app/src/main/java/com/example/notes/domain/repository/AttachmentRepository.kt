package com.example.notes.domain.repository

import com.example.notes.domain.model.Attachment
import kotlinx.coroutines.flow.Flow

interface AttachmentRepository {
    suspend fun insertAttachment(attachment: Attachment)

    suspend fun updateAttachment(attachment: Attachment)

    suspend fun deleteAttachmentById(id: Int)

    suspend fun deleteAttachmentByNoteId(noteId: Int)

    fun getAttachmentsByNoteId(noteId: Int): Flow<List<Attachment>>

    suspend fun getAttachmentByNoteIdAndUri(
        noteId: Int,
        uri: String,
    ): Attachment?

    suspend fun getAttachmentByTimeStamp(timeStamp: Long): Attachment?
}
