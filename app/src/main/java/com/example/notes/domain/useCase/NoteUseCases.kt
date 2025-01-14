package com.example.notes.domain.useCase

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNote: GetNoteUseCase,
    val insertNote: InsertNoteUseCase,
    val getRecentlyDeletedNotes: GetRecentlyDeletedNotesUseCase
)
