package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.NoteWithAttachments
import com.example.notes.domain.repository.NoteRepository

class GetNoteByTimeStampUseCase(
    private val repository: NoteRepository,
) {
    suspend operator fun invoke(timeStamp: Long): NoteWithAttachments? {
        return repository.getNoteByTimeStamp(timeStamp)
    }
}
