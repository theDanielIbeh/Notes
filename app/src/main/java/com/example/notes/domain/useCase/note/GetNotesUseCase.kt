package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.NoteWithAttachments
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetNotesUseCase(
    private val repository: NoteRepository,
) {

    operator fun invoke(query: String): Flow<List<NoteWithAttachments>> {
        return repository.getActiveNotes().map { notesWithAttachments ->
            notesWithAttachments.filter { withAttachments ->
                withAttachments.note.title.contains(query, ignoreCase = true) ||
                        withAttachments.note.content.contains(query, ignoreCase = true)
            }
        }
    }

}