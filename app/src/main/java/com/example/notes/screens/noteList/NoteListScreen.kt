package com.example.notes.screens.noteList

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.ui.theme.NotesTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteListScreen(
    viewModel: NoteListViewModel = koinViewModel<NoteListViewModel>(),
    onNoteClick: (Int?) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showMenu by remember { mutableStateOf(false) }
    var showSearchBar by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.getNotes() }
    // Main Background
    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNoteClick(null) },
                modifier = modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    Icons.Filled.Add,
                    "Add",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background) // Light mode background
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
//                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                // Header Section
                HeaderSection()

                Spacer(modifier = Modifier.height(16.dp))

                // Search Bar
//                AnimatedVisibility(
//                    visible = showSearchBar,
//                    modifier = Modifier.fillMaxWidth(),
//                    enter = slideInVertically(),
//                    exit = slideOutVertically()
//                ) {
                SearchBar(
                    value = state.query,
                    onValueChange = { viewModel.onEvent(NoteListEvent.SearchNote(it)) }
                )
//                }

                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDrag = { change, _ ->
                                change.consume()
                            },
                            onDragStart = {
                                showSearchBar = !showSearchBar
                            },
                        )
                    }
                ) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            key = { it.id ?: 0 },
                            items = state.notes
                        ) { note ->
                            NoteItem(
                                note,
                                onClick = {
                                    onNoteClick(note.id)
                                },
                                onLongClick = {
                                    viewModel.onEvent(NoteListEvent.SelectNote(note))
                                    showMenu = true
                                }
                            )
                        }
                    }
                }
            }
        }
        if (showMenu) {
            NoteOptionsMenu(
                onDismiss = { showMenu = false },
                onShareClick = { /* Handle Share Action */ },
                onDeleteClick = {
                    state.selectedNote?.id?.let { viewModel.onEvent(NoteListEvent.DeleteNote(it)) }
                    coroutineScope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Note Deleted",
                            actionLabel = "Undo",
                            duration = SnackbarDuration.Short
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(NoteListEvent.RestoreNote)
                        }
                    }
                    showMenu = false
                }
            )
        }

        // Footer for Storage Warning
//        StorageWarning(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = null,
                    modifier = Modifier
                        .height(36.dp)
                        .width(24.dp)
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets(0.dp)),
                    tint = MaterialTheme.colorScheme.tertiaryContainer
                )
                Text(
                    text = "Folders",
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    fontSize = 14.sp
                )
            }
        },
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(end = 16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.wrapContentWidth()
                ) {
//                    Icon(
//                        painter = painterResource(R.drawable.search),
//                        contentDescription = null,
//                        modifier = Modifier
//                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Icon(
                        painter = painterResource(R.drawable.ic_profile),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.tertiaryContainer
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
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
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun HeaderSection(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "All iCloud",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    BasicTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .background(
                shape = RoundedCornerShape(50.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            ),
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            fontSize = 16.sp
        ),
        interactionSource = interactionSource
    ) { innerTextField ->
        TextFieldDefaults.DecorationBox(
            value = value,
            innerTextField = innerTextField,
            placeholder = {
                Text(
                    text = "Search",
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp
                )
            },
            enabled = true,
            singleLine = true,
            visualTransformation = VisualTransformation.None,
            interactionSource = interactionSource,
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.search),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            },
            trailingIcon = {},
            container = {},
            contentPadding = PaddingValues(0.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteOptionsMenu(
    onDismiss: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = sheetState
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.share), modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onShareClick() }
                    .padding(16.dp))
            HorizontalDivider()
            Text(
                stringResource(R.string.delete),
                color = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onDeleteClick() }
                    .padding(16.dp)
            )
        }
    }
}


@Preview
@Composable
fun PreviewNotesScreen() {
    NotesTheme(darkTheme = false) {
        NoteListScreen()
    }
}