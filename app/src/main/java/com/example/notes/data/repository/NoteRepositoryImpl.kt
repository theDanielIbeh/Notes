package com.example.notes.data.repository

import android.app.Application
import androidx.core.net.toUri
import com.example.notes.data.dataSource.attachment.AttachmentDao
import com.example.notes.data.dataSource.note.NoteDao
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.Note
import com.example.notes.domain.model.NoteWithAttachments
import com.example.notes.domain.model.service.DatabaseService
import com.example.notes.domain.model.service.impl.AttachmentStorageServiceImpl
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.screens.util.FileUtils.DOCUMENTS
import com.example.notes.screens.util.FileUtils.PICTURES
import com.example.notes.screens.util.FileUtils.createFilesDirectory
import com.example.notes.screens.util.FileUtils.getFileName
import com.example.notes.screens.util.FileUtils.isImageFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File


class NoteRepositoryImpl(
    private val application: Application,
    private val noteDao: NoteDao,
    private val attachmentDao: AttachmentDao,
    private val noteDatabaseService: DatabaseService<Note>,
    private val attachmentDatabaseService: DatabaseService<Attachment>,
    private val attachmentStorageService: AttachmentStorageServiceImpl
    ) : NoteRepository {

    override fun getNotes(): Flow<List<NoteWithAttachments>> {
        return noteDao.getNotes()
    }

    override fun getActiveNotes(): Flow<List<NoteWithAttachments>> {
        return flow {
            // Step 1: Fetch and update local storage only if new data is available
            noteDatabaseService.data.collectLatest { notes ->
                val localNotes = noteDao.getNotes().first()

                notes.forEach { note ->
                    if (note.id in localNotes.map { it.note.id }) {
                        noteDao.updateNote(note = note)
                    } else {
                        noteDao.insertNote(note = note)
                    }
                }
                fetchAttachments()
            }

            // Step 2: Emit active notes from the local database (always show stored data first)
            emitAll(noteDao.getActiveNotes())
        }
    }

    private suspend fun fetchAttachments() {
        attachmentDatabaseService.data.collectLatest { attachments ->
            val localNotes = noteDao.getNotes().first()
            attachments.forEach { attachment ->
                if (attachment.noteId in localNotes.map { it.note.id }) {
                    attachmentDao.insertAttachment(attachment = attachment)
                    val file = File(attachment.uri)
                    val directoryName = if (isImageFile(context = application, uri = attachment.uri.toUri())) {
                        createFilesDirectory(application, PICTURES)
                    } else {
                        createFilesDirectory(application, DOCUMENTS)
                    }
                    if (!file.exists()) {
                        getFileName(
                            application,
                            attachment.uri.toUri()
                        )?.let { attachmentStorageService.downloadFile(it, directoryName) }
                    }
                }
            }
        }
    }

    override fun getRecentlyDeletedNotes(timestamp: Long): Flow<List<NoteWithAttachments>> {
        return noteDao.getRecentlyDeletedNotes(timestamp)
    }

    override suspend fun getNoteById(id: Int): NoteWithAttachments? {
        return noteDao.getNoteById(id)
    }

    override suspend fun getNoteByTimeStamp(timeStamp: Long): NoteWithAttachments? {
        return noteDao.getNoteByTimeStamp(timeStamp)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)

        val uploadedNote = noteDao.getNoteByTimeStamp(note.timeStamp)

        uploadedNote?.let { noteDatabaseService.create(it.note) }
    }

    override suspend fun updateNote(note: Note) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteNoteById(id: Int) {
        noteDao.deleteNoteById(id)

        noteDatabaseService.delete(id.toString())
    }
}