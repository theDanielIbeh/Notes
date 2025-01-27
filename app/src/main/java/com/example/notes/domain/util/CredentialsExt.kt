package com.example.notes.domain.util

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.notes.R
import com.example.notes.SnackBarManager
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import kotlinx.coroutines.launch

const val ERROR_TAG = "Unexpected type of credential"

@Composable
fun AuthenticationButton(
    buttonText: Int,
    loadingText: String = "Signing in...",
    onRequestResult: (Credential) -> Unit,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var clicked by rememberSaveable { mutableStateOf(false) }

    Button(
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.tertiaryContainer),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSecondaryContainer),
        onClick = {
            clicked = !clicked
            coroutineScope.launch { launchCredManButtonUI(context, onRequestResult) }
        },
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(start = 12.dp, end = 16.dp, top = 12.dp, bottom = 12.dp)
    ) {
        if (isSystemInDarkTheme()) {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo_outlined),
                modifier = Modifier.padding(horizontal = 16.dp),
                contentDescription = "Google logo",
                tint = Color.Unspecified
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.ic_google_logo),
                modifier = Modifier.padding(horizontal = 16.dp),
                contentDescription = "Google logo",
                tint = Color.Unspecified
            )
        }

        Text(
            text = if (clicked) loadingText else stringResource(buttonText),
            color = MaterialTheme.colorScheme.tertiaryContainer,
            fontSize = 16.sp,
            modifier = Modifier.padding(6.dp)
        )

        if (clicked) {
            Spacer(modifier = Modifier.width(16.dp))
            CircularProgressIndicator(
                modifier = Modifier
                    .height(16.dp)
                    .width(16.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colorScheme.tertiaryContainer
            )
        }
    }
}

private suspend fun launchCredManButtonUI(
    context: Context,
    onRequestResult: (Credential) -> Unit
) {
    try {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = context.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        onRequestResult(result.credential)
    } catch (e: NoCredentialException) {
        Log.d(ERROR_TAG, e.message.orEmpty())
        SnackBarManager.showMessage(context.getString(R.string.no_accounts_error))
    } catch (e: GetCredentialException) {
        Log.d(ERROR_TAG, e.message.orEmpty())
    }
}

suspend fun launchCredManBottomSheet(
    context: Context,
    hasFilter: Boolean = true,
    onRequestResult: (Credential) -> Unit
) {
    try {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(hasFilter)
            .setServerClientId(context.getString(R.string.web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        val result = CredentialManager.create(context).getCredential(
            request = request,
            context = context
        )

        onRequestResult(result.credential)
    } catch (e: NoCredentialException) {
        Log.d(ERROR_TAG, e.message.orEmpty())

        //If the bottom sheet was launched with filter by authorized accounts, we launch it again
        //without filter so the user can see all available accounts, not only the ones that have
        //been previously authorized in this app
        if (hasFilter) {
            launchCredManBottomSheet(context, hasFilter = false, onRequestResult)
        }
    } catch (e: GetCredentialException) {
        Log.d(ERROR_TAG, e.message.orEmpty())
    }
}