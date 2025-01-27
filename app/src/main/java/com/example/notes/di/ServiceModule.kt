package com.example.notes.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.notes.domain.model.service.AccountService
import com.example.notes.domain.model.service.StorageService
import com.example.notes.domain.model.service.impl.AccountServiceImpl
import com.example.notes.domain.model.service.NotesPreferencesRepository
import com.example.notes.domain.model.service.impl.NotesPreferencesRepositoryImpl
import com.example.notes.domain.model.service.impl.StorageServiceImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::AccountServiceImpl) { bind<AccountService>() }
    singleOf(::StorageServiceImpl) { bind<StorageService>() }
    singleOf(::NotesPreferencesRepositoryImpl) { bind<NotesPreferencesRepository>() }
    singleOf(::provideDataStore)
}

const val NOTES_PREFERENCE_NAME = "notes_preferences"
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = NOTES_PREFERENCE_NAME
)

fun provideDataStore(context: Context): DataStore<Preferences> = context.dataStore