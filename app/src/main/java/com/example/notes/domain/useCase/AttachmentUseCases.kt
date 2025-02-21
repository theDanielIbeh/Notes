package com.example.notes.domain.useCase

import com.example.notes.domain.useCase.attachment.DeleteAttachmentUseCase
import com.example.notes.domain.useCase.attachment.GetAttachmentByNoteIdAndUriUseCase
import com.example.notes.domain.useCase.attachment.GetAttachmentsUseCase
import com.example.notes.domain.useCase.attachment.InsertAttachmentUseCase

data class AttachmentUseCases(
    val getAttachments: GetAttachmentsUseCase,
    val deleteAttachment: DeleteAttachmentUseCase,
    val insertAttachment: InsertAttachmentUseCase,
    val getAttachmentByNoteIdAndUri: GetAttachmentByNoteIdAndUriUseCase,
)
