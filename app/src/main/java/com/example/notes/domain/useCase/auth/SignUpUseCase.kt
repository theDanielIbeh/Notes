package com.example.notes.domain.useCase.auth

import com.example.notes.domain.model.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpUseCase(
    private val accountService: AccountService,
    private val auth: FirebaseAuth,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
    ) {
        accountService.createUserWithEmailAndPassword(email, password)
        val user = auth.currentUser
        val profileUpdate =
            UserProfileChangeRequest.Builder()
                .setDisplayName("${firstName.trim()} ${lastName.trim()}")
                .build()
        user?.updateProfile(profileUpdate)

//        try {
//            accountService.linkAccountWithEmail(email, password)
//            val user = auth.currentUser
//            val profileUpdate = UserProfileChangeRequest.Builder()
//                .setDisplayName("${.firstName.trim()} ${.lastName.trim()}")
//                .build()
//            user?.updateProfile(profileUpdate)
//            signUpState.update {
//                it.copy(
//                    isRegisterSuccessful = true,
//                    isRegisterFailed = false
//                )
//            }
//        } catch (e: FirebaseAuthException) {
//            signUpState.update {
//                it.copy(
//                    isRegisterFailed = true,
//                    isRegisterSuccessful = false
//                )
//            }
//        }
    }
}
