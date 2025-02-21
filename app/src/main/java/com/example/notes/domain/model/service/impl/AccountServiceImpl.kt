package com.example.notes.domain.model.service.impl

import com.example.notes.domain.model.User
import com.example.notes.domain.model.service.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class AccountServiceImpl(
    private val auth: FirebaseAuth
) : AccountService {

    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser.toNotesUser())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return auth.currentUser != null
    }

    override fun getUserProfile(): User {
        return auth.currentUser.toNotesUser()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun createUserWithEmailAndPassword(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun updateDisplayName(newDisplayName: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = newDisplayName
        }

        auth.currentUser!!.updateProfile(profileUpdates).await()
    }

    override suspend fun linkAccountWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.currentUser!!.linkWithCredential(firebaseCredential).await()
    }

    override suspend fun linkAccountWithEmail(email: String, password: String) {
        createAnonymousAccount()
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
//        auth.currentUser!!.linkWithCredential(credential).addOnSuccessListener {
//            Log.d("Success", it.toString())
//        }.addOnFailureListener {
//            Log.e("Error", it.message.toString())
//        }
    }

    override suspend fun signInWithGoogle(idToken: String) {
        val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(firebaseCredential).await()
    }

    override suspend fun signInWithEmail(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signOut() {
        auth.signOut()

        // Sign the user back in anonymously.
        createAnonymousAccount()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    private fun FirebaseUser?.toNotesUser(): User {
        return if (this == null) User() else User(
            id = this.uid,
            email = this.email ?: "",
            provider = this.providerId,
            displayName = this.displayName ?: "",
            isAnonymous = this.isAnonymous
        )
    }
}