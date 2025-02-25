package com.example.notes.di

import android.app.Application
import androidx.room.Room
import com.example.notes.data.dataSource.NoteDatabase
import com.example.notes.data.dataSource.NoteDatabase.Companion.DATABASE_NAME
import com.example.notes.data.dataSource.attachment.AttachmentDao
import com.example.notes.data.dataSource.note.NoteDao
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val dataSourceModule =
    module {
        singleOf(::provideNoteDao) // Provide Application context
        singleOf(::provideDatabase)
        singleOf(::provideAttachmentDao)
    }

fun provideDatabase(application: Application): NoteDatabase =
    Room.databaseBuilder(
        application,
        NoteDatabase::class.java,
        DATABASE_NAME,
    ).build()

fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao

fun provideAttachmentDao(database: NoteDatabase): AttachmentDao = database.attachmentDao
