package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyDeletedNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(timestamp: Long): Flow<List<Note>> {
        return repository.getRecentlyDeletedNotes(timestamp)
    }
}