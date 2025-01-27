package com.example.notes.screens.note

import com.example.notes.domain.model.Note

sealed class NoteEvent {
    data class GetNote(val id: Int) : NoteEvent()
    data class SaveNote(val note: Note, val popUpScreen: (() -> Unit)? = null) : NoteEvent()
    data class DeleteNote(val noteId: Int) : NoteEvent()
    data object RestoreNote : NoteEvent()
    data class EditTitle(val title: String) : NoteEvent()
    data class EditContent(val content: String) : NoteEvent()
}
