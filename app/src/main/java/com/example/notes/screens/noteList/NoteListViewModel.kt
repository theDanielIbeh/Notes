package com.example.notes.screens.noteList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.useCase.AttachmentUseCases
import com.example.notes.domain.useCase.NoteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val noteUseCases: NoteUseCases,
    private val attachmentUseCases: AttachmentUseCases
) : ViewModel() {

    var state = MutableStateFlow(NoteListState())
        private set

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.DeleteNote -> {
                viewModelScope.launch {
                    state.value = state.value.copy(
                        recentlyDeletedNote = noteUseCases.getNote(event.noteId)
                    )
                    noteUseCases.deleteNote(event.noteId)
                }
            }

            is NoteListEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.insertNote(state.value.recentlyDeletedNote?.note ?: return@launch)
                    state.value.recentlyDeletedNote?.attachments?.forEach {
                        attachmentUseCases.insertAttachment(it)
                    }
                    state.value = state.value.copy(
                        recentlyDeletedNote = null
                    )
                }
            }

            NoteListEvent.ToggleSearchBar -> {
                state.value = state.value.copy(
                    isSearchBarVisible = !state.value.isSearchBarVisible
                )
            }

            is NoteListEvent.SelectNote -> {
                state.value = state.value.copy(
                    selectedNoteWithAttachments = event.note
                )
            }

            is NoteListEvent.SearchNote -> {
                state.value = state.value.copy(
                    query = event.query
                )
                getNotes()
            }
        }
    }

    fun getNotes() {
        viewModelScope.launch {
            noteUseCases.getNotes(state.value.query).collectLatest { notesWithAttachments ->
                state.value = state.value.copy(
                    notesWithAttachments = notesWithAttachments
                )
                Log.d("NotesWithAttachments", notesWithAttachments.toString())
            }
        }
    }
}