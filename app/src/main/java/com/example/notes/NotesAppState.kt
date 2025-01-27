package com.example.notes

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
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
        navController.popBackStack()
    }

    fun <T : Any> navigate(route: T) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun <T : Any> navigateAndPopUp(route: T, popUp: T) {
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
