package com.example.notes.domain.useCase.auth

import com.example.notes.domain.model.service.AccountService

class CreateAnonymousUseCase(
    private val accountService: AccountService
) {
    suspend operator fun invoke() {
        accountService.createAnonymousAccount()
    }
}