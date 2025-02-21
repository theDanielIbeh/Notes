package com.example.notes.domain.useCase.note

import com.example.notes.domain.model.InvalidNoteException
import com.example.notes.domain.model.Note
import com.example.notes.domain.repository.NoteRepository

class InsertNoteUseCase(
    private val repository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty.")
        }
//        if (note.content.isBlank()) {
//            throw InvalidNoteException("The content of the note can't be empty.")
//        }
        if (note.id == null) {
            repository.insertNote(note)
        } else {
            repository.updateNote(note)
        }
    }
}