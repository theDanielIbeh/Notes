package com.example.notes.domain.useCase.note

import androidx.core.net.toUri
import com.example.notes.domain.repository.AttachmentRepository
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.screens.util.FileUtils.deleteFileFromUri

class DeleteNoteUseCase(
    private val noteRepository: NoteRepository,
    private val attachmentRepository: AttachmentRepository
) {
    suspend operator fun invoke(noteId: Int) {
        noteRepository.deleteNoteById(noteId)
        val noteAttachments = attachmentRepository.getAttachmentsByNoteId(noteId)
        noteAttachments.collect { attachments ->
            attachments.forEach { attachment ->
                deleteFileFromUri(attachment.uri.toUri())
            }
        }
        attachmentRepository.deleteAttachmentByNoteId(noteId)
    }
}