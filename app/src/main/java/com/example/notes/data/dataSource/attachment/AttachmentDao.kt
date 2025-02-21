package com.example.notes.data.dataSource.attachment

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notes.domain.model.Attachment
import com.example.notes.domain.model.AttachmentTableConstants
import kotlinx.coroutines.flow.Flow

@Dao
interface AttachmentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttachment(attachment: Attachment)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAttachment(attachment: Attachment)

    @Query("DELETE FROM ${AttachmentTableConstants.ATTACHMENT_TABLE} WHERE ${AttachmentTableConstants.ID} = :id")
    suspend fun deleteAttachmentById(id: Int)

    @Query("DELETE FROM ${AttachmentTableConstants.ATTACHMENT_TABLE} WHERE ${AttachmentTableConstants.NOTE_ID} = :noteId")
    suspend fun deleteAttachmentByNoteId(noteId: Int)

    @Query("SELECT * FROM ${AttachmentTableConstants.ATTACHMENT_TABLE} WHERE ${AttachmentTableConstants.ID} = :noteId ORDER BY ${AttachmentTableConstants.TIME_STAMP} DESC")
    fun getAttachmentsByNoteId(noteId: Int): Flow<List<Attachment>>

    @Query("SELECT * FROM ${AttachmentTableConstants.ATTACHMENT_TABLE} WHERE ${AttachmentTableConstants.NOTE_ID} = :noteId AND ${AttachmentTableConstants.URI} = :uri")
    suspend fun getAttachmentByNoteIdAndUri(noteId: Int, uri: String): Attachment?

    @Query("SELECT * FROM ${AttachmentTableConstants.ATTACHMENT_TABLE} WHERE ${AttachmentTableConstants.TIME_STAMP} = :timeStamp")
    suspend fun getAttachmentByTimeStamp(timeStamp: Long): Attachment?
}