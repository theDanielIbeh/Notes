package com.example.notes.domain.useCase

import com.example.notes.domain.useCase.note.DeleteNoteUseCase
import com.example.notes.domain.useCase.note.GetNoteByTimeStampUseCase
import com.example.notes.domain.useCase.note.GetNoteUseCase
import com.example.notes.domain.useCase.note.GetNotesUseCase
import com.example.notes.domain.useCase.note.GetRecentlyDeletedNotesUseCase
import com.example.notes.domain.useCase.note.InsertNoteUseCase

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNote: GetNoteUseCase,
    val getNoteByTimeStamp: GetNoteByTimeStampUseCase,
    val insertNote: InsertNoteUseCase,
    val getRecentlyDeletedNotes: GetRecentlyDeletedNotesUseCase
)
