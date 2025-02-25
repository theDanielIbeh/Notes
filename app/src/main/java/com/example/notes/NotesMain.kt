package com.example.notes

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notes.screens.note.NoteScreen
import com.example.notes.screens.noteList.NoteListScreen
import com.example.notes.screens.signIn.SignInScreen
import com.example.notes.screens.signUp.SignUpScreen
import com.example.notes.screens.util.Constants.NOTE_DEFAULT_ID
import com.example.notes.screens.util.Constants.NOTE_ID
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.Serializable

@Composable
fun NotesMain(modifier: Modifier = Modifier) {
    Surface(color = MaterialTheme.colorScheme.background, modifier = modifier) {
        val snackbarHostState = remember { SnackbarHostState() }
        val appState = rememberAppState(snackbarHostState)

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { innerPaddingModifier ->
            NavHost(
                navController = appState.navController,
                startDestination = if (FirebaseAuth.getInstance().currentUser == null) Route.Login else Route.NotesList,
                modifier = Modifier.padding(innerPaddingModifier),
            ) {
                notesGraph(appState)
            }
        }
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState,
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackBarManager = SnackBarManager,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): NotesAppState {
    return remember(snackbarHostState, navController, snackbarManager, coroutineScope) {
        NotesAppState(snackbarHostState, navController, snackbarManager, coroutineScope)
    }
}

fun NavGraphBuilder.notesGraph(appState: NotesAppState) {
    composable<Route.NotesList> {
        NoteListScreen(
            onNoteClick = { noteId -> appState.navigate(Route.Note(noteId)) },
        )
    }

    composable<Route.Note> { backStackEntry ->
        val noteId = backStackEntry.arguments?.getInt(NOTE_ID) ?: NOTE_DEFAULT_ID
        NoteScreen(
            noteId = noteId,
            popUpScreen = { appState.popUp() },
        )
    }

    composable<Route.Login> {
        SignInScreen(
            onNavigateToSignUp = { appState.clearAndNavigate(Route.Register) },
            openAndPopUp = {
                appState.clearAndNavigate(Route.NotesList)
            },
        )
    }

    composable<Route.Register> {
        SignUpScreen(
            onNavigateToSignIn = { appState.clearAndNavigate(Route.Login) },
            openAndPopUp = {
                appState.clearAndNavigate(Route.NotesList)
            },
        )
    }
}

sealed interface Route {
    @Serializable
    data object Login : Route

    @Serializable
    data object Register : Route

    @Serializable
    data object NotesList : Route

    @Serializable
    data class Note(val noteId: Int?) : Route
}
