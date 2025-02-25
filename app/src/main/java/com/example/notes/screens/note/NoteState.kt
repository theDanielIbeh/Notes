package com.example.notes.screens.note

import com.example.notes.domain.model.NoteWithAttachments

data class NoteState(
    val noteWithAttachments: NoteWithAttachments = NoteWithAttachments(),
    val error: String? = null,
    val recentlyDeletedNote: NoteWithAttachments? = null,
)
