package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.NoteWithAttachments
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class GetRecentlyDeletedNotesUseCase(
    private val repository: NoteRepository
) {
    operator fun invoke(timestamp: Long): Flow<List<NoteWithAttachments>> {
        return repository.getRecentlyDeletedNotes(timestamp)
    }
}