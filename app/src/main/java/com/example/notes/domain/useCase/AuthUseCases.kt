package com.example.notes.domain.useCase

import com.example.notes.domain.useCase.auth.CreateAnonymousUseCase
import com.example.notes.domain.useCase.auth.SignInUseCase
import com.example.notes.domain.useCase.auth.SignInWithGoogleUseCase
import com.example.notes.domain.useCase.auth.SignUpUseCase
import com.example.notes.domain.useCase.auth.SignUpWithGoogleUseCase

data class AuthUseCases(
    val signIn: SignInUseCase,
    val signUp: SignUpUseCase,
    val signUpWithGoogle: SignUpWithGoogleUseCase,
    val signInWithGoogle: SignInWithGoogleUseCase,
    val createAnonymous: CreateAnonymousUseCase,
)
