package com.example.notes.domain.model.service

import androidx.lifecycle.LiveData
import com.google.gson.Gson

interface NotesPreferencesRepository {
    val gson: Gson

    fun <T : Any?> getPreference(
        keyClassType: Class<T>,
        key: String,
    ): LiveData<T?>

    suspend fun <T> savePreference(
        key: String,
        value: T,
    ): Boolean

    suspend fun clearDataStore()
}
