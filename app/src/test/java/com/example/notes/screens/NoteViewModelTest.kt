package com.example.notes.screens

import com.example.notes.domain.repository.AttachmentRepository
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.domain.useCase.AttachmentUseCases
import com.example.notes.domain.useCase.NoteUseCases
import com.example.notes.domain.useCase.attachment.DeleteAttachmentUseCase
import com.example.notes.domain.useCase.attachment.GetAttachmentByNoteIdAndUriUseCase
import com.example.notes.domain.useCase.attachment.GetAttachmentsUseCase
import com.example.notes.domain.useCase.attachment.InsertAttachmentUseCase
import com.example.notes.domain.useCase.note.DeleteNoteUseCase
import com.example.notes.domain.useCase.note.GetNoteByTimeStampUseCase
import com.example.notes.domain.useCase.note.GetNoteUseCase
import com.example.notes.domain.useCase.note.GetNotesUseCase
import com.example.notes.domain.useCase.note.GetRecentlyDeletedNotesUseCase
import com.example.notes.domain.useCase.note.InsertNoteUseCase
import com.example.notes.screens.note.NoteEvent
import com.example.notes.screens.note.NoteState
import com.example.notes.screens.note.NoteViewModel
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class NoteViewModelTest {
    private lateinit var viewModel: NoteViewModel
    private lateinit var noteUseCases: NoteUseCases
    private lateinit var attachmentUseCases: AttachmentUseCases
    private lateinit var repository: NoteRepository
    private lateinit var attachmentRepository: AttachmentRepository

    @Before
    fun setup() {
        repository = mockk<NoteRepository>(relaxed = true)
        attachmentRepository = mockk<AttachmentRepository>(relaxed = true)

        noteUseCases = NoteUseCases(
            getNotes = GetNotesUseCase(repository),
            deleteNote = DeleteNoteUseCase(repository, attachmentRepository),
            insertNote = InsertNoteUseCase(repository),
            getNote = GetNoteUseCase(repository),
            getNoteByTimeStamp = GetNoteByTimeStampUseCase(repository),
            getRecentlyDeletedNotes = GetRecentlyDeletedNotesUseCase(repository)
        )

        attachmentUseCases = AttachmentUseCases(
            getAttachments = GetAttachmentsUseCase(attachmentRepository),
            deleteAttachment = DeleteAttachmentUseCase(attachmentRepository),
            insertAttachment = InsertAttachmentUseCase(attachmentRepository),
            getAttachmentByNoteIdAndUri = GetAttachmentByNoteIdAndUriUseCase(attachmentRepository)
        )

        viewModel =
            NoteViewModel(
                noteUseCases = noteUseCases,
                attachmentUseCases = attachmentUseCases
            )
    }

    @Test
    fun `test edit title`() {
        viewModel.onEvent(NoteEvent.EditTitle("New Title"))
        val currentNoteState: NoteState = viewModel.state.value
        assert(currentNoteState.noteWithAttachments.note.title == "New Title")
    }

    @Test
    fun `test edit content`() {
        viewModel.onEvent(NoteEvent.EditContent("New Content"))
        val currentNoteState: NoteState = viewModel.state.value
        assert(currentNoteState.noteWithAttachments.note.content == "New Content")
    }

//    @Test
//    fun `test save note`() {
//        val note = Note(
//            id = -1,
//            title= "Dummy",
//            content = "Content"
//        )
//        viewModel.onEvent(NoteEvent.SaveNote(note = note))
//
//    }
}