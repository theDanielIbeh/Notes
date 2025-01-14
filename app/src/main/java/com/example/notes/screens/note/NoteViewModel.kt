package com.example.notes.screens.note

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.useCase.NoteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    var state = MutableStateFlow(NoteState())
        private set

//    fun onEvent(event: NoteListEvent) {
//        when (event) {
//            is NoteListEvent.DeleteNote -> {
//                viewModelScope.launch {
//                    noteUseCases.deleteNote(event.noteId)
//                    state.value = state.value.copy(
//                        recentlyDeletedNote = noteUseCases.getNote(event.noteId)
//                    )
//                }
//            }
//
//            is NoteListEvent.RestoreNote -> {
//                viewModelScope.launch {
//                    noteUseCases.insertNote(state.value.recentlyDeletedNote ?: return@launch)
//                    state.value = state.value.copy(
//                        recentlyDeletedNote = null
//                    )
//                }
//            }
//
//            NoteListEvent.ToggleSearchBar -> {
//                state.value = state.value.copy(
//                    isSearchBarVisible = !state.value.isSearchBarVisible
//                )
//            }
//
//            is NoteListEvent.SelectNote -> {
//                state.value = state.value.copy(
//                    selectedNote = event.note
//                )
//            }
//
//            is NoteListEvent.SearchNote -> {
//                state.value = state.value.copy(
//                    query = event.query
//                )
//                getNotes()
//            }
//        }
//    }

    private fun getNote() {
        viewModelScope.launch {
            noteUseCases.getNote(id = 1).also { note ->
                state.value = state.value.copy(
                    note = note
                )
            }
        }
    }
}