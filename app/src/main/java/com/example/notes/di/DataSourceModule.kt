package com.example.notes.di

import android.app.Application
import androidx.room.Room
import com.example.notes.data.dataSource.NoteDao
import com.example.notes.data.dataSource.NoteDatabase
import com.example.notes.domain.util.Constants.DATABASE_NAME
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule = module {
    single { provideDatabase(get()) } // Provide Application context
    single { provideNoteDao(get()) }
}

fun provideDatabase(application: Application) : NoteDatabase =
    Room.databaseBuilder(
        application,
        NoteDatabase::class.java,
        DATABASE_NAME
    ).build()

fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao