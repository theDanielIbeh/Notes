package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteWithAttachments
import com.example.notes.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): NoteWithAttachments? {
        return repository.getNoteById(id)
    }
}