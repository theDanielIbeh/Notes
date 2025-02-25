package com.example.notes.screens.noteList

import com.example.notes.domain.model.NoteWithAttachments

sealed class NoteListEvent {
    data class DeleteNote(val noteId: Int) : NoteListEvent()

    data class SelectNote(val note: NoteWithAttachments) : NoteListEvent()

    data class SearchNote(val query: String) : NoteListEvent()

    data object RestoreNote : NoteListEvent()

    data object ToggleSearchBar : NoteListEvent()
}
