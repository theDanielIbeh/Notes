package com.example.notes

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class NotesAppState(
    private val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackBarManager,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackBarMessages.filterNotNull().collect { message ->
                snackbarHostState.showSnackbar(message)
                snackbarManager.clearSnackBarState()
            }
        }
    }

    fun popUp() {
        val popped = navController.popBackStack()
        Log.d("Navigation", "popBackStack() called: $popped")
    }

    fun <T : Any> navigate(route: T) {
        navController.navigate(route)
    }

    fun <T : Any> navigateAndPopUp(route: T, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun <T : Any> clearAndNavigate(route: T) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}
