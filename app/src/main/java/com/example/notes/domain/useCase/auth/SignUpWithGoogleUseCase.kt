package com.example.notes.domain.useCase.auth

import com.example.notes.domain.model.service.AccountService

class SignUpWithGoogleUseCase(
    private val accountService: AccountService,
) {
    suspend operator fun invoke(idToken: String) {
        accountService.linkAccountWithGoogle(idToken)
    }
}
