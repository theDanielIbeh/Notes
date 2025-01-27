package com.example.notes.di

import com.example.notes.screens.note.NoteViewModel
import com.example.notes.screens.noteList.NoteListViewModel
import com.example.notes.screens.signIn.SignInViewModel
import com.example.notes.screens.signUp.SignUpViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::NoteListViewModel)
    viewModelOf(::NoteViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::SignUpViewModel)
}