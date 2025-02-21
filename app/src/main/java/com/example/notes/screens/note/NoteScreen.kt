package com.example.notes.screens.note

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.example.notes.R
import com.example.notes.screens.util.FileUtils.DOCUMENTS
import com.example.notes.screens.util.FileUtils.PICTURES
import com.example.notes.screens.util.FileUtils.createFileFromUri
import com.example.notes.screens.util.FileUtils.createFilesDirectory
import com.example.notes.screens.util.FileUtils.getUriFromFile
import com.example.notes.screens.util.FileUtils.isImageFile
import com.example.notes.screens.util.Helper.convertTimestampToFormattedDate
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteScreen(
    noteId: Int,
    viewModel: NoteViewModel = koinViewModel<NoteViewModel>(),
    application: Application = LocalContext.current.applicationContext as Application,
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val attachments by viewModel.attachmentUris.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            if (isImageFile(context = application, uri = it)) {
                val createdFile = createFileFromUri(
                    context = application,
                    directoryName = createFilesDirectory(application, PICTURES),
                    uri = it
                )
                createdFile?.let { file ->
                    val attachmentUri = getUriFromFile(application, file)
                    attachmentUri?.let { viewModel.onEvent(NoteEvent.AddAttachment(attachmentUri)) }
                }
            } else {
                val createdFile = createFileFromUri(
                    context = application,
                    directoryName = createFilesDirectory(application, DOCUMENTS),
                    uri = it
                )
                createdFile?.let { file ->
                    val attachmentUri = getUriFromFile(application, file)
                    attachmentUri?.let { viewModel.onEvent(NoteEvent.AddAttachment(attachmentUri)) }
                }
            }
        }
    }

    var showDate by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(noteId) { viewModel.getNote(noteId) }

    Scaffold(
        topBar = {
            AppBar(
                onBackPressed = {
                    viewModel.onEvent(
                        NoteEvent.SaveNote(
                            noteWithAttachments = state.noteWithAttachments,
                            popUpScreen = popUpScreen
                        )
                    )
                },
                onDoneClicked = {
                    viewModel.onEvent(
                        NoteEvent.SaveNote(
                            noteWithAttachments = state.noteWithAttachments,
                        )
                    )
                },
                onDeleteClicked = {
                    state.noteWithAttachments.note.id?.let {
                        viewModel.onEvent(
                            NoteEvent.DeleteNote(
                                noteId = it
                            )
                        )
                    }
                    popUpScreen()
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomToolbar { selectedOption ->
                // Handle option selection
                when (selectedOption) {
                    BottomToolbarOptions.Checklist.name -> { /* Navigate to checklist screen */
                    }

                    BottomToolbarOptions.Attachment.name -> { /* Open attachment picker */
                        filePickerLauncher.launch("*/*")
                    }

                    BottomToolbarOptions.Draw.name -> { /* Open drawing canvas */
                    }

                    BottomToolbarOptions.Edit.name -> { /* Enable editing mode */
                    }
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            NoteContent(
                date = convertTimestampToFormattedDate(state.noteWithAttachments.note.timeStamp),
                title = state.noteWithAttachments.note.title,
                content = state.noteWithAttachments.note.content,
                editTitle = { viewModel.onEvent(NoteEvent.EditTitle(it)) },
                editContent = { viewModel.onEvent(NoteEvent.EditContent(it)) },
                showDate = showDate,
                toggleDate = { showDate = !showDate }
            )
            Log.d("Attachments", attachments.toString())
            if (attachments.isNotEmpty()) {
                AttachmentPreview(
                    application,
                    attachments.toList(),
                    onRemove = { viewModel.onEvent(NoteEvent.DeleteAttachment(it)) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onBackPressed: () -> Unit,
    onDoneClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .clickable { onBackPressed() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .height(36.dp)
                        .width(24.dp)
                        .fillMaxWidth(),
                    tint = MaterialTheme.colorScheme.tertiaryContainer
                )

                Text(
                    text = "All Notes",
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    fontSize = 14.sp,
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Spacer(modifier = Modifier.weight(1F))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
//                    Icon(
//                        painter = ,
//                        contentDescription = null,
//                        modifier = Modifier,
//                        tint = MaterialTheme.colorScheme.tertiaryContainer
//                    )
//                    Icon(
//                        painter = painterResource(R.drawable.ic_ellipsis),
//                        contentDescription = null,
//                        modifier = Modifier.size(24.dp),
//                        tint = MaterialTheme.colorScheme.tertiaryContainer
//                    )
                    DropdownMenu(
                        onShareClick = { /* Handle Share Action */ },
                        onDeleteClick = onDeleteClicked

                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Done",
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        fontSize = 14.sp,
                        modifier = Modifier.clickable { onDoneClicked() },
                    )
                }
            }
        },
        windowInsets = WindowInsets(0.dp),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun DropdownMenu(
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_ellipsis),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clickable { expanded = !expanded },
            tint = MaterialTheme.colorScheme.tertiaryContainer
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.share)) },
                onClick = { onShareClick() }
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.delete)) },
                onClick = { onDeleteClick() }
            )
        }
    }
}

@Composable
fun BottomToolbar(
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    val toolbarOptions = listOf(
        BottomToolbarOptions.Checklist to R.drawable.ic_checklist,
        BottomToolbarOptions.Attachment to R.drawable.ic_attachment,
        BottomToolbarOptions.Draw to R.drawable.ic_draw,
        BottomToolbarOptions.Edit to R.drawable.ic_edit
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        toolbarOptions.forEach { (option, iconRes) ->
            ToolbarIconButton(iconRes = iconRes, contentDescription = option.name) {
                onOptionSelected(option.name)
            }
        }
    }
}

@Composable
fun ToolbarIconButton(iconRes: Int, contentDescription: String, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.tertiaryContainer,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

enum class BottomToolbarOptions {
    Checklist,
    Attachment,
    Draw,
    Edit
}