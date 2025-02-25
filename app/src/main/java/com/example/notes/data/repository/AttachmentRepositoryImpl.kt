package com.example.notes.data.repository

import com.example.notes.data.dataSource.attachment.AttachmentDao
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.service.DatabaseService
import com.example.notes.domain.repository.AttachmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AttachmentRepositoryImpl(
    private val attachmentDao: AttachmentDao,
    private val attachmentDatabaseService: DatabaseService<Attachment>,
) : AttachmentRepository {
    override suspend fun insertAttachment(attachment: Attachment) {
        attachmentDao.insertAttachment(attachment)

        val uploadedAttachment = attachmentDao.getAttachmentByTimeStamp(attachment.timeStamp)

        uploadedAttachment?.let { attachmentDatabaseService.create(it) }
    }

    override suspend fun updateAttachment(attachment: Attachment) {
        attachmentDao.updateAttachment(attachment)
    }

    override suspend fun deleteAttachmentById(id: Int) {
        attachmentDao.deleteAttachmentById(id)
        attachmentDatabaseService.delete(id.toString())
    }

    override suspend fun deleteAttachmentByNoteId(noteId: Int) {
        attachmentDao.deleteAttachmentByNoteId(noteId)
        attachmentDao.getAttachmentsByNoteId(noteId).first().forEach { attachment ->
            attachmentDatabaseService.delete(attachment.id.toString())
        }
    }

    override fun getAttachmentsByNoteId(noteId: Int): Flow<List<Attachment>> {
        return flow {
            attachmentDatabaseService.data.collectLatest { attachments ->
                val localAttachments = attachmentDao.getAttachmentsByNoteId(noteId).first()

                attachments.forEach { attachment ->
                    if (attachment.id in localAttachments.map { it.id }) {
                        updateAttachment(attachment = attachment)
                    } else {
                        insertAttachment(attachment = attachment)
                    }
                }
            }
            emitAll(attachmentDao.getAttachmentsByNoteId(noteId))
        }
    }

    override suspend fun getAttachmentByNoteIdAndUri(
        noteId: Int,
        uri: String,
    ): Attachment? {
        return attachmentDao.getAttachmentByNoteIdAndUri(noteId, uri)
    }

    override suspend fun getAttachmentByTimeStamp(timeStamp: Long): Attachment? {
        return attachmentDao.getAttachmentByTimeStamp(timeStamp)
    }
}
