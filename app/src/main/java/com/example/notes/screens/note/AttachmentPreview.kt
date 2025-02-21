package com.example.notes.screens.note

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.notes.R
import com.example.notes.screens.util.FileUtils
import com.example.notes.screens.util.FileUtils.getFileName

@Composable
fun AttachmentPreview(context: Context, attachments: List<Uri>, onRemove: (Uri) -> Unit) {
    LazyColumn(
        modifier = Modifier.padding(8.dp).fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = attachments,
            key = { it.toString() }
        ) { uri ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray)
                    .clickable { FileUtils.openFile(context, uri) },
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (FileUtils.isImageFile(context, uri)) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(uri)
                                .crossfade(true)
                                .placeholder(R.drawable.place_holder_image)
                                .error(R.drawable.error_image)
                                .build(),
                            contentDescription = "Attachment",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            painter = painterResource(R.drawable.ic_document),
                            contentDescription = "Document",
                            tint = Color.Black,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    Text(
                        text = getFileName(context, uri) ?: "",
                        modifier = Modifier.padding(8.dp)
                    )
                }

                IconButton(
                    onClick = { onRemove(uri) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.Red)
                }
            }
        }
    }
}