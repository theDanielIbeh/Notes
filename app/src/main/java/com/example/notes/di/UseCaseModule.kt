package com.example.notes.di

import com.example.notes.domain.useCase.DeleteNoteUseCase
import com.example.notes.domain.useCase.GetNoteUseCase
import com.example.notes.domain.useCase.GetNotesUseCase
import com.example.notes.domain.useCase.GetRecentlyDeletedNotesUseCase
import com.example.notes.domain.useCase.InsertNoteUseCase
import com.example.notes.domain.useCase.NoteUseCases
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetNotesUseCase)
    factoryOf(::GetNoteUseCase)
    factoryOf(::InsertNoteUseCase)
    factoryOf(::DeleteNoteUseCase)
    factoryOf(::GetRecentlyDeletedNotesUseCase)

    factoryOf(::NoteUseCases)
}