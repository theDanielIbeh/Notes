package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository

class GetNoteByTimeStampUseCase(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(timeStamp: Long): Note? {
        return repository.getNoteByTimeStamp(timeStamp)
    }
}