package com.example.notes.screens.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.notes.R
import com.example.notes.ui.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun NoteScreen(
    navController: NavController = rememberNavController(),
    viewModel: NoteViewModel = koinViewModel<NoteViewModel>(),
    modifier: Modifier = Modifier
) {
    var showDate by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = { AppBar() },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomToolbar { selectedOption ->
                // Handle option selection
                when (selectedOption) {
                    "Checklist" -> { /* Navigate to checklist screen */
                    }

                    "Attachment" -> { /* Open attachment picker */
                    }

                    "Draw" -> { /* Open drawing canvas */
                    }

                    "Edit" -> { /* Enable editing mode */
                    }
                }
            }
        }
    ) { innerPadding ->
        NoteContent(
            date = "5 January 2025 at 13:14",
            title = "Tomorrow's Tasks",
            content =
            "6:00 - 7:00: Pray and bathe" +
                    "7:00 - 8:00: Data Structures" +
                    "8:00 - 10:00: TfL video interview" +
                    "10:00 - 11:00: Android Login page",
            toggleDate = { showDate = !showDate },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun NoteContent(
    date: String,
    title: String,
    content: String,
    toggleDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        AnimatedVisibility(
            visible = true,
            modifier = Modifier.fillMaxWidth(),
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Text(
                text = date,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
        BasicTextField(
            value = title,
            onValueChange = { /* Handle title change */ },
            textStyle = MaterialTheme.typography.headlineLarge,
            maxLines = 1,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
        )
        BasicTextField(
            value = content,
            onValueChange = { /* Handle content change */ },
            textStyle = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
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
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_back_arrow),
                contentDescription = null,
                modifier = Modifier
                    .height(36.dp)
                    .width(24.dp)
                    .fillMaxWidth(),
                tint = MaterialTheme.colorScheme.tertiaryContainer
            )
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Folders",
                        color = MaterialTheme.colorScheme.tertiaryContainer,
                        fontSize = 14.sp
                    )
                }
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
        windowInsets = WindowInsets(
            left = 0.dp,
        ),
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
        IconButton(onClick = { onOptionSelected("Checklist") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Checklist",
                tint = Color(0xFFFFD700)
            )
        }
        IconButton(onClick = { onOptionSelected("Attachment") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Attachment",
                tint = Color(0xFFFFD700)
            )
        }
        IconButton(onClick = { onOptionSelected("Draw") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Draw",
                tint = Color(0xFFFFD700)
            )
        }
        IconButton(onClick = { onOptionSelected("Edit") }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back_arrow), // Replace with actual resource
                contentDescription = "Edit",
                tint = Color(0xFFFFD700)
            )
        }
    }
}

@Preview
@Composable
fun PreviewNoteScreen() {
    NotesTheme(darkTheme = false) {
        NoteScreen()
    }
}