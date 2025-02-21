package com.example.notes.di

import com.example.notes.domain.useCase.AttachmentUseCases
import com.example.notes.domain.useCase.AuthUseCases
import com.example.notes.domain.useCase.note.DeleteNoteUseCase
import com.example.notes.domain.useCase.note.GetNoteUseCase
import com.example.notes.domain.useCase.note.GetNotesUseCase
import com.example.notes.domain.useCase.note.GetRecentlyDeletedNotesUseCase
import com.example.notes.domain.useCase.note.InsertNoteUseCase
import com.example.notes.domain.useCase.NoteUseCases
import com.example.notes.domain.useCase.attachment.DeleteAttachmentUseCase
import com.example.notes.domain.useCase.attachment.GetAttachmentByNoteIdAndUriUseCase
import com.example.notes.domain.useCase.attachment.GetAttachmentsUseCase
import com.example.notes.domain.useCase.attachment.InsertAttachmentUseCase
import com.example.notes.domain.useCase.auth.CreateAnonymousUseCase
import com.example.notes.domain.useCase.auth.SignInUseCase
import com.example.notes.domain.useCase.auth.SignInWithGoogleUseCase
import com.example.notes.domain.useCase.auth.SignUpUseCase
import com.example.notes.domain.useCase.auth.SignUpWithGoogleUseCase
import com.example.notes.domain.useCase.note.GetNoteByTimeStampUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factoryOf(::GetNotesUseCase)
    factoryOf(::GetNoteUseCase)
    factoryOf(::GetNoteByTimeStampUseCase)
    factoryOf(::InsertNoteUseCase)
    factoryOf(::DeleteNoteUseCase)
    factoryOf(::GetRecentlyDeletedNotesUseCase)

    factoryOf(::SignInUseCase)
    factoryOf(::SignUpUseCase)
    factoryOf(::SignInWithGoogleUseCase)
    factoryOf(::SignUpWithGoogleUseCase)
    factoryOf(::CreateAnonymousUseCase)

    factoryOf(::GetAttachmentsUseCase)
    factoryOf(::DeleteAttachmentUseCase)
    factoryOf(::InsertAttachmentUseCase)
    factoryOf(::GetAttachmentByNoteIdAndUriUseCase)

    singleOf(::NoteUseCases)
    singleOf(::AuthUseCases)
    singleOf(::AttachmentUseCases)
}