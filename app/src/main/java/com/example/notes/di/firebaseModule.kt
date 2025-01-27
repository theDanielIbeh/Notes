package com.example.notes.di

import com.example.notes.domain.model.NoteTableConstants.NOTE_TABLE
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule = module {
    singleOf(::provideAuth)
    singleOf(::provideFireDatabase)
    singleOf(::provideStorage)
    singleOf(::provideStorageReference)
}

fun provideAuth(): FirebaseAuth = Firebase.auth
fun provideFireDatabase(): FirebaseDatabase = Firebase.database
fun provideStorage(): FirebaseStorage = Firebase.storage
fun provideStorageReference(database: FirebaseDatabase): DatabaseReference = database.reference.child(NOTE_TABLE)