package com.example.notes.screens.note

import com.example.notes.domain.model.Note

data class NoteState(
    val note: Note? = null,
    val error: String? = null,
)
