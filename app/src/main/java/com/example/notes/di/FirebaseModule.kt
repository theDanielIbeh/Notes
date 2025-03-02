package com.example.notes.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule =
    module {
        singleOf(::provideAuth)
        singleOf(::provideStorage)
        singleOf(::provideFireDatabase)
        singleOf(::provideStorageReference)
        singleOf(::provideDatabaseReference)
    }

fun provideAuth(): FirebaseAuth = Firebase.auth

fun provideStorage(): FirebaseStorage = Firebase.storage

fun provideFireDatabase(): FirebaseDatabase = Firebase.database

fun provideStorageReference(storage: FirebaseStorage): StorageReference = storage.reference

fun provideDatabaseReference(database: FirebaseDatabase): DatabaseReference = database.reference
