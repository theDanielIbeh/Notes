package com.example.notes.data

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.notes.data.dataSource.NoteDatabase
import com.example.notes.data.dataSource.note.NoteDao
import com.example.notes.domain.model.Note
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class NoteDaoTest {
    private lateinit var dao: NoteDao
    private lateinit var db: NoteDatabase

    @Before
    fun setup() {
        db =
            Room.inMemoryDatabaseBuilder(
                ApplicationProvider.getApplicationContext(),
                NoteDatabase::class.java,
            ).build()
        dao = db.noteDao
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getNotes_WhenNotesExist_ReturnsAllNotes() =
        runTest {
            // Arrange
            val note1 =
                Note(id = 1, title = "Note 1", content = "Content 1", timeStamp = 12345, deleteFlag = 0)
            val note2 =
                Note(id = 2, title = "Note 2", content = "Content 2", timeStamp = 67890, deleteFlag = 0)
            dao.insertNote(note1)
            dao.insertNote(note2)

            // Act
            val notesWithAttachments = dao.getNotes().first()
            val notes = notesWithAttachments.map { it.note }

            // Assert
            assertEquals(2, notesWithAttachments.size)

            assertTrue(notes.contains(note1))
            assertTrue(notes.contains(note2))
        }
}
