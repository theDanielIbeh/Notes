package com.example.notes.screens.note

import android.net.Uri
import com.example.notes.domain.model.NoteWithAttachments

sealed class NoteEvent {
    data class GetNote(val id: Int) : NoteEvent()
    data class SaveNote(val noteWithAttachments: NoteWithAttachments, val popUpScreen: (() -> Unit)? = null) : NoteEvent()
    data class DeleteNote(val noteId: Int) : NoteEvent()
    data object RestoreNote : NoteEvent()
    data class AddAttachment(val uri: Uri) : NoteEvent()
    data class DeleteAttachment(val uri: Uri) : NoteEvent()
    data class EditTitle(val title: String) : NoteEvent()
    data class EditContent(val content: String) : NoteEvent()
}
