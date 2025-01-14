package com.example.notes.domain.useCase

import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository

class GetNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note? {
        return repository.getNoteById(id)
    }
}