package com.example.notes.di

import com.example.notes.screens.note.NoteViewModel
import com.example.notes.screens.noteList.NoteListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NoteListViewModel)
    viewModelOf(::NoteViewModel)
}