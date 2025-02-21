package com.example.notes.domain.model.service

import kotlinx.coroutines.flow.Flow

interface DatabaseService<T> {
    val data: Flow<List<T>>
    suspend fun create(item: T)
    suspend fun read(itemId: String): T?
    suspend fun update(item: T)
    suspend fun delete(itemId: String)
}