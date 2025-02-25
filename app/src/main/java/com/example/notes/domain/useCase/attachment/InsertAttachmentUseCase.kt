package com.example.notes.domain.useCase.attachment

import android.app.Application
import androidx.core.net.toUri
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.service.impl.AttachmentStorageServiceImpl
import com.example.notes.domain.repository.AttachmentRepository
import com.example.notes.screens.util.FileUtils.getFileName
import com.example.notes.screens.util.FileUtils.getMimeType

class InsertAttachmentUseCase(
    private val application: Application,
    private val attachmentRepository: AttachmentRepository,
    private val attachmentStorageService: AttachmentStorageServiceImpl,
) {
    suspend operator fun invoke(attachment: Attachment) {
        attachmentRepository.insertAttachment(attachment)
        attachmentStorageService.uploadFile(
            attachment.uri,
            getFileName(
                context = application,
                uri = attachment.uri.toUri(),
            ),
            getMimeType(attachment.uri.toUri()),
        )
    }
}
