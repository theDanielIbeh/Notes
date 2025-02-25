package com.example.notes.domain.useCase.auth

import com.example.notes.domain.model.service.AccountService

class SignInWithGoogleUseCase(
    private val accountService: AccountService,
) {
    suspend operator fun invoke(idToken: String) {
        accountService.signInWithGoogle(idToken)
    }
}
