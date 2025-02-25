package com.example.notes.domain.useCase.attachment

import com.example.notes.domain.model.Attachment
import com.example.notes.domain.repository.AttachmentRepository

class GetAttachmentByNoteIdAndUriUseCase(
    private val repository: AttachmentRepository,
) {
    suspend operator fun invoke(
        noteId: Int,
        uri: String,
    ): Attachment? {
        return repository.getAttachmentByNoteIdAndUri(noteId, uri)
    }
}
