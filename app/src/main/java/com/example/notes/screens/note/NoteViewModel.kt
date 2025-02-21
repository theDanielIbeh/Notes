package com.example.notes.screens.note

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.useCase.AttachmentUseCases
import com.example.notes.domain.useCase.NoteUseCases
import com.example.notes.screens.util.FileUtils.deleteFileFromUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteUseCases: NoteUseCases,
    private val attachmentUseCases: AttachmentUseCases
) : ViewModel() {

    var state = MutableStateFlow(NoteState())
        private set

    var attachmentUris = MutableStateFlow<Set<Uri>>(emptySet())
        private set

    fun onEvent(event: NoteEvent) {
        when (event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.noteId)
                    state.value = state.value.copy(
                        recentlyDeletedNote = noteUseCases.getNote(event.noteId)
                    )
                }
            }

            is NoteEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.insertNote(state.value.recentlyDeletedNote?.note ?: return@launch)
                    state.value = state.value.copy(
                        recentlyDeletedNote = null
                    )
                }
            }

            is NoteEvent.GetNote -> {
                getNote(event.id)
            }

            is NoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        val existingAttachmentUris = state.value.noteWithAttachments.attachments
                            ?.map { it.uri }
                            ?: emptyList()

                        noteUseCases.insertNote(event.noteWithAttachments.note)
                        state.value = state.value.copy(
                            noteWithAttachments = noteUseCases.getNoteByTimeStamp(event.noteWithAttachments.note.timeStamp)
                                ?: return@launch
                        )

                        Log.d("ExistingAttachments", existingAttachmentUris.toString())
                        val addedAttachmentUris = attachmentUris.value.filter { attachmentUri ->
                            attachmentUri.toString() !in existingAttachmentUris
                        }
                        Log.d("AddedAttachments", addedAttachmentUris.toString())
                        val deletedAttachments =
                            state.value.noteWithAttachments.attachments?.filter { attachment ->
                                attachment.uri !in attachmentUris.value.map { it.toString() }
                            } ?: emptyList()
                        Log.d("DeletedAttachments", deletedAttachments.toString())
                        addedAttachmentUris.forEach {
                            attachmentUseCases.insertAttachment(
                                Attachment(
                                    noteId = state.value.noteWithAttachments.note.id!!,
                                    uri = it.toString()
                                )
                            )
                            val attachment = attachmentUseCases.getAttachmentByNoteIdAndUri(
                                noteId = state.value.noteWithAttachments.note.id!!,
                                uri = it.toString()
                            )
                            state.value = state.value.copy(
                                noteWithAttachments = state.value.noteWithAttachments.copy(
                                    attachments = attachment?.let {
                                        state.value.noteWithAttachments.attachments?.plus(
                                            it
                                        )
                                    }
                                )
                            )

                        }

                        deletedAttachments.forEach { attachment ->
                            attachmentUseCases.deleteAttachment(attachment)
                        }
                    } catch (e: Exception) {
                        if (state.value.noteWithAttachments.note.id != null) {
                            e.message?.let { Log.e("InvalidNoteException", it) }
                        }
                        Log.e("InvalidNoteException", e.toString())
                    }
                    event.popUpScreen?.let { it() }
                }
            }

            is NoteEvent.EditTitle -> {
                state.value = state.value.copy(
                    noteWithAttachments = state.value.noteWithAttachments.copy(
                        note = state.value.noteWithAttachments.note.copy(
                            title = event.title,
                            timeStamp = System.currentTimeMillis()
                        )
                    )
                )
            }

            is NoteEvent.EditContent -> {
                state.value = state.value.copy(
                    noteWithAttachments = state.value.noteWithAttachments.copy(
                        note = state.value.noteWithAttachments.note.copy(
                            content = event.content,
                            timeStamp = System.currentTimeMillis()
                        )
                    )
                )
            }

            is NoteEvent.AddAttachment -> {
                attachmentUris.value += event.uri
                state.value = state.value.copy(
                    noteWithAttachments = state.value.noteWithAttachments.copy(
                        note = state.value.noteWithAttachments.note.copy(
                            timeStamp = System.currentTimeMillis()
                        )
                    )
                )
            }

            is NoteEvent.DeleteAttachment -> {
                attachmentUris.value -= event.uri
                state.value = state.value.copy(
                    noteWithAttachments = state.value.noteWithAttachments.copy(
                        note = state.value.noteWithAttachments.note.copy(
                            timeStamp = System.currentTimeMillis()
                        )
                    )
                )
            }
        }
    }

    fun getNote(id: Int) {
        viewModelScope.launch {
            noteUseCases.getNote(id = id)?.also { noteWithAttachments ->
                Log.d("NotesWithAttachments", noteWithAttachments.toString())
                state.value = state.value.copy(
                    noteWithAttachments = noteWithAttachments
                )
                attachmentUris.value =
                    (noteWithAttachments.attachments?.map { it.uri.toUri() } ?: emptyList()).toSet()
            }
        }
    }
}