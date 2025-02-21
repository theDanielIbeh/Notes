package com.example.notes.domain.useCase.attachment

import android.app.Application
import android.util.Log
import androidx.core.net.toUri
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.service.impl.AttachmentStorageServiceImpl
import com.example.notes.domain.repository.AttachmentRepository
import com.example.notes.domain.repository.NoteRepository
import com.example.notes.screens.note.NoteEvent
import com.example.notes.screens.util.FileUtils.deleteFileFromUri
import com.example.notes.screens.util.FileUtils.getFileName

class DeleteAttachmentUseCase(
    private val application: Application,
    private val attachmentRepository: AttachmentRepository,
    private val attachmentStorageService: AttachmentStorageServiceImpl
) {
    suspend operator fun invoke(attachment: Attachment) {
        deleteFileFromUri(attachment.uri.toUri())
        Log.d("DeleteAttachment", attachment.toString())
        attachment.id?.let { attachmentRepository.deleteAttachmentById(it) }
        getFileName(application, attachment.uri.toUri())?.let {
            attachmentStorageService.deleteFile(it)
        }
    }
}