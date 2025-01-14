package com.example.notes.screens.noteList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.useCase.NoteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    var state = MutableStateFlow(NoteListState())
        private set

    fun onEvent(event: NoteListEvent) {
        when (event) {
            is NoteListEvent.DeleteNote -> {
                viewModelScope.launch {
                    noteUseCases.deleteNote(event.noteId)
                    state.value = state.value.copy(
                        recentlyDeletedNote = noteUseCases.getNote(event.noteId)
                    )
                }
            }

            is NoteListEvent.RestoreNote -> {
                viewModelScope.launch {
                    noteUseCases.insertNote(state.value.recentlyDeletedNote ?: return@launch)
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
                    selectedNote = event.note
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

    private fun getNotes() {
        viewModelScope.launch {
            noteUseCases.getNotes(state.value.query).collect { notes ->
                state.value = state.value.copy(
                    notes = notes
                )
            }
        }
    }
}