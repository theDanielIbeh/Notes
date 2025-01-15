package com.example.notes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object SnackBarManager {
    private val messages: MutableStateFlow<String?> = MutableStateFlow(null)
    val snackBarMessages: StateFlow<String?>
        get() = messages

    fun showMessage(message: String) {
        messages.value = message
    }

    fun clearSnackBarState() {
        messages.value = null
    }
}