package com.example.notes.screens.signUp

import androidx.credentials.Credential

sealed class SignUpEvent {
    data class SignUp(val popUpScreen: () -> Unit) : SignUpEvent()

    data class SignUpWithGoogle(val credential: Credential, val popUpScreen: () -> Unit) : SignUpEvent()

    data class EditFirstName(val fName: String) : SignUpEvent()

    data class EditLastName(val lName: String) : SignUpEvent()

    data class EditEmail(val email: String) : SignUpEvent()

    data class EditPassword(val password: String) : SignUpEvent()

    data class EditConfirmPassword(val password: String) : SignUpEvent()

    data class EditPhone(val phone: String) : SignUpEvent()

    data object ResetState : SignUpEvent()
}
