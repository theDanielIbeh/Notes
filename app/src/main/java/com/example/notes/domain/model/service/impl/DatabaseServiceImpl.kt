package com.example.notes.domain.model.service.impl

import android.util.Log
import com.example.notes.domain.model.service.DatabaseService
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class DatabaseServiceImpl<T>(
    reference: DatabaseReference,
    auth: FirebaseAuth,
    tableName: String,
    private val fromSnapshot: (DataSnapshot) -> T?,
    private val toMap: (T) -> Map<String, Any?>,
) : DatabaseService<T> {
    private val reference = reference.child(tableName).child(auth.currentUser!!.uid)

    override val data: Flow<List<T>>
        get() =
            flow {
                try {
                    Log.d("firebaseRed", "Getting data from reference: $reference")
                    val snapshot = reference.get().await()
                    val result = snapshot.children.mapNotNull { fromSnapshot(it) }
                    emit(result)
                } catch (e: FirebaseException) {
                    Log.e("firebase", "Error getting data", e)
                    emit(emptyList())
                }
            }

    override suspend fun create(item: T) {
        val itemData = toMap(item)
        reference.get().addOnSuccessListener { snapshot ->
            val noteSnapshot =
                snapshot.children.filter {
                    it.child("id").value.toString().toInt() == itemData["id"]
                }
            if (noteSnapshot.isEmpty()) {
                reference.push().setValue(itemData)
            } else {
                reference.child(noteSnapshot[0].key.toString()).setValue(itemData)
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }
//        reference.push().setValue(itemData)
//            .addOnFailureListener { Log.e("firebase", "Error saving data", it) }
    }

    override suspend fun read(itemId: String): T? {
        return try {
            val snapshot = reference.child(itemId).get().await()
            fromSnapshot(snapshot)
        } catch (e: Exception) {
            Log.e("firebase", "Error reading data", e)
            null
        }
    }

    override suspend fun update(item: T) {
        val itemData = toMap(item)
        reference.get().addOnSuccessListener { snapshot ->
            val itemSnapshot =
                snapshot.children.find {
                    it.child("id").value.toString() == itemData["id"].toString()
                }
            itemSnapshot?.key?.let { key ->
                reference.child(key).setValue(itemData)
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error updating data", it)
        }
    }

    override suspend fun delete(itemId: String) {
        Log.d("firebase", "Deleting item with ID: $itemId")
        reference.get().addOnSuccessListener { snapshot ->
            val itemSnapshot =
                snapshot.children.find {
                    it.child("id").value.toString() == itemId
                }
            itemSnapshot?.key?.let { key ->
                reference.child(key).removeValue()
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error deleting data", it)
        }
    }
}
