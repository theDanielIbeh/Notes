package com.example.notes.screens.note

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.domain.model.InvalidNoteException
import com.example.notes.domain.useCase.NoteUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class NoteViewModel(
    private val application: Application,
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    var state = MutableStateFlow(NoteState())
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
                    noteUseCases.insertNote(state.value.recentlyDeletedNote ?: return@launch)
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
                        noteUseCases.insertNote(event.note)
                        state.value = state.value.copy(
                            note = noteUseCases.getNoteByTimeStamp(event.note.timeStamp)
                                ?: return@launch
                        )
                    } catch (e: InvalidNoteException) {
                        if (state.value.note.id != null) {
                            Toast.makeText(application, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                    event.popUpScreen?.let { it() }
                }
            }

            is NoteEvent.EditTitle -> {
                state.value = state.value.copy(
                    note = state.value.note.copy(
                        title = event.title,
                        timeStamp = System.currentTimeMillis()
                    )
                )
            }

            is NoteEvent.EditContent -> {
                state.value = state.value.copy(
                    note = state.value.note.copy(
                        content = event.content,
                        timeStamp = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    fun getNote(id: Int) {
        viewModelScope.launch {
            noteUseCases.getNote(id = id)?.also { note ->
                state.value = state.value.copy(
                    note = note
                )
            }
        }
    }
}