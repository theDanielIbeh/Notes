package com.example.notes.domain.useCase.auth

import com.example.notes.domain.model.service.AccountService

class SignInUseCase(
    private val accountService: AccountService
) {
    suspend operator fun invoke(email: String, password: String) {
        accountService.signInWithEmail(email, password)
    }
}