package com.example.notes.data.repository

import com.example.notes.data.dataSource.NoteDao
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.service.StorageService
import com.example.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class NoteRepositoryImpl(
    private val noteDao: NoteDao,
    private val storageService: StorageService
) : NoteRepository {
    override fun getNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    override fun getActiveNotes(): Flow<List<Note>> {
//        reference.get().addOnSuccessListener { snapshot ->
//            Log.i(
//                "firebase", "Got value ${
//                    snapshot.children.map {
//                        Note(
//                            id = it.child(NoteTableConstants.ID).value.toString().toInt(),
//                            title = it.child(NoteTableConstants.TITLE).value.toString(),
//                            content = it.child(NoteTableConstants.CONTENT).value.toString(),
//                            timeStamp = it.child(NoteTableConstants.TIME_STAMP).value.toString()
//                                .toLong(),
//                            deleteFlag = it.child(NoteTableConstants.DELETE_FLAG).value.toString()
//                                .toInt()
//                        )
//                    }
//                }"
//            )
//        }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
//        }

//        val postListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // Get Post object and use the values to update the UI
//                val post = dataSnapshot.children
//                // ...
//                Log.i("firebase", "${post.map {
//                        Note(
//                            id = it.key.toString().toInt(),
//                            title = it.child(NoteTableConstants.TITLE).value.toString(),
//                            content = it.child(NoteTableConstants.CONTENT).value.toString(),
//                            timeStamp = it.child(NoteTableConstants.TIME_STAMP).value.toString()
//                                .toLong(),
//                            deleteFlag = it.child(NoteTableConstants.DELETE_FLAG).value.toString()
//                                .toInt()
//                        )
//                    }}")
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Getting Post failed, log a message
//                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
//            }
//        }
//        reference.addValueEventListener(postListener)

        return flow {
            // Step 1: Fetch and insert notes from the storage service
            storageService.notes.collectLatest { notes ->
                notes.forEach { note -> noteDao.insertNote(note) }
            }

            // Step 2: Emit active notes from the local database
            emitAll(noteDao.getActiveNotes())
        }
    }

    override fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<Note>> {
        return noteDao.getRecentlyDeletedNotes(timestamp)
    }

    override suspend fun getNoteById(id: Int): Note? {
        return noteDao.getNoteById(id)
    }

    override suspend fun getNoteByTimeStamp(timeStamp: Long): Note? {
        return noteDao.getNoteByTimeStamp(timeStamp)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)

        val uploadedNote = noteDao.getNoteByTimeStamp(note.timeStamp)

        uploadedNote?.let { storageService.createNote(it) }
    }

    override suspend fun deleteNoteById(id: Int) {
        noteDao.deleteNoteById(id)

        storageService.deleteNote(id.toString())
//        reference.get().addOnSuccessListener { snapshot ->
//            val note = snapshot.children.filter {
//                it.child(NoteTableConstants.ID).value.toString().toInt() == id
//            }
//            reference.child(note[0].key.toString()).removeValue()
//        }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
//        }
    }
}