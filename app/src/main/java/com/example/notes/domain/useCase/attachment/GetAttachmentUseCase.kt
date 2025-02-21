package com.example.notes.domain.useCase.attachment

import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.NoteWithAttachments
import com.example.notes.domain.repository.AttachmentRepository
import kotlinx.coroutines.flow.Flow

class GetAttachmentsUseCase(
    private val repository: AttachmentRepository
) {
    operator fun invoke(id: Int): Flow<List<Attachment>> {
        return repository.getAttachmentsByNoteId(id)
    }
}