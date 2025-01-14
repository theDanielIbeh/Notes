package com.example.notes.screens.noteList

import com.example.notes.domain.model.Note

data class NoteListState(
    val notes: List<Note> = emptyList(),
    val selectedNote: Note? = null,
    val recentlyDeletedNote: Note? = null,
    val isSearchBarVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val query: String = ""
)
