package com.example.notes.di

import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.notes.WorkManagerRepository
import com.example.notes.data.repository.AttachmentRepositoryImpl
import com.example.notes.data.repository.NoteRepositoryImpl
import com.example.notes.domain.repository.AttachmentRepository
import com.example.notes.domain.repository.NoteRepository
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single<NoteRepository> {
        NoteRepositoryImpl(
            get(),
            get(),
            get(),
            get(named("noteStorageService")),
            get(named("attachmentStorageService")),
            get()
        )
    }
    single<AttachmentRepository> {
        AttachmentRepositoryImpl(
            get(),
            get(named("attachmentStorageService"))
        )
    }
    singleOf(::provideConfiguration)
    singleOf(::provideWorkManager)
    singleOf(::WorkManagerRepository)
//    singleOf(::NoteRepositoryImpl) { bind<NoteRepository>() }
//    singleOf(::AttachmentRepositoryImpl) { bind<AttachmentRepository>() }
}

private fun provideConfiguration():Configuration =
    Configuration.Builder()
        .setMinimumLoggingLevel(android.util.Log.DEBUG)
        .build()

private fun provideWorkManager(context: Context, config: Configuration):WorkManager {
    WorkManager.initialize(context, config)
    return WorkManager.getInstance(context)
}