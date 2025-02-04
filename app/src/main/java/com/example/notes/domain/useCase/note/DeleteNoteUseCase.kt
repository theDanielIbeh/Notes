package com.example.notes.domain.useCase.note

import com.example.notes.domain.repository.NoteRepository

class DeleteNoteUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(noteId: Int) {
        repository.deleteNoteById(noteId)
    }
}