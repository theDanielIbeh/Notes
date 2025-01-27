package com.example.notes.screens.signIn

import androidx.credentials.Credential
import com.example.notes.domain.model.Note
import com.example.notes.screens.note.NoteEvent

sealed class SignInEvent {
    data class SignIn(val popUpScreen: () -> Unit) : SignInEvent()
    data class SignInWithGoogle(val credential: Credential, val popUpScreen: () -> Unit) : SignInEvent()
    data class EditEmail(val email: String) : SignInEvent()
    data class EditPassword(val password: String) : SignInEvent()
    data object ResetState : SignInEvent()
}
