package com.example.notes.screens.note

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.domain.util.Helper.convertTimestampToFormattedDate
import org.koin.androidx.compose.koinViewModel
import java.util.Date

@Composable
fun NoteScreen(
    noteId: Int,
    viewModel: NoteViewModel = koinViewModel<NoteViewModel>(),
    popUpScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    var showDate by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(noteId) { viewModel.getNote(noteId) }

    Scaffold(
        topBar = {
            AppBar(
                onBackPressed = {
                    viewModel.onEvent(
                        NoteEvent.SaveNote(
                            note = state.note,
                            popUpScreen = popUpScreen
                        )
                    )
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
        NoteContent(
            date = convertTimestampToFormattedDate(state.note.timeStamp),
            title = state.note.title,
            content = state.note.content,
            editTitle = { viewModel.onEvent(NoteEvent.EditTitle(it)) },
            editContent = { viewModel.onEvent(NoteEvent.EditContent(it)) },
            showDate = showDate,
            toggleDate = { showDate = !showDate },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContent(
    date: String,
    title: String,
    content: String,
    editTitle: (String) -> Unit,
    editContent: (String) -> Unit,
    showDate: Boolean,
    toggleDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }

//    LaunchedEffect(title.isNotEmpty()) {
//        focusRequester.requestFocus()
//    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AnimatedVisibility(
            visible = showDate,
            modifier = Modifier.fillMaxWidth(),
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Text(
                text = date,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
        BasicTextField(
            value = title,
            onValueChange = { editTitle(it) },
            textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            interactionSource = interactionSource,
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = "Title",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                container = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
        BasicTextField(
            value = content,
            enabled = title.isNotEmpty(),
            onValueChange = { editContent(it) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(0.dp)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, _ ->
                            change.consume()
                        },
                        onDragStart = {
                            toggleDate()
                        },
                    )
                }
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = "Content",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                container = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
            ) {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .height(36.dp)
                    .width(24.dp)
                    .fillMaxWidth()
                    .clickable { onBackPressed() },
                tint = MaterialTheme.colorScheme.tertiaryContainer
            )

                Text(
                    text = "All Notes",
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBackPressed() },
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
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(R.drawable.ic_ellipsis),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiaryContainer
                    )
                }
            }
        },
        windowInsets = WindowInsets(0.dp),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun BottomToolbar(
    modifier: Modifier = Modifier,
    onOptionSelected: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { onOptionSelected(BottomToolbarOptions.Checklist.name) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Checklist",
                tint = Color(0xFFFFD700)
            )
        }
        IconButton(onClick = { onOptionSelected(BottomToolbarOptions.Attachment.name) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Attachment",
                tint = Color(0xFFFFD700)
            )
        }
        IconButton(onClick = { onOptionSelected(BottomToolbarOptions.Draw.name) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Draw",
                tint = Color(0xFFFFD700)
            )
        }
        IconButton(onClick = { onOptionSelected(BottomToolbarOptions.Edit.name) }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Edit",
                tint = Color(0xFFFFD700)
            )
        }
    }
}

enum class BottomToolbarOptions {
    Checklist,
    Attachment,
    Draw,
    Edit
}