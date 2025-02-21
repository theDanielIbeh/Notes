package com.example.notes.screens.noteList

import com.example.notes.domain.model.NoteWithAttachments

data class NoteListState(
    val notesWithAttachments: List<NoteWithAttachments> = emptyList(),
    val selectedNoteWithAttachments: NoteWithAttachments? = null,
    val recentlyDeletedNote: NoteWithAttachments? = null,
    val isSearchBarVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = ""
)
