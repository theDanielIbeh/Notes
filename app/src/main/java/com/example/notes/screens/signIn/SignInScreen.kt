package com.example.notes.screens.signIn

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.screens.common.AuthTextField
import com.example.notes.screens.util.AuthenticationButton
import com.example.notes.screens.util.Helper.isConnected
import com.example.notes.screens.util.launchCredManBottomSheet
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignInScreen(
    application: Application = LocalContext.current.applicationContext as Application,
    onNavigateToSignUp: () -> Unit,
    openAndPopUp: () -> Unit,
    modifier: Modifier = Modifier,
    loginViewModel: SignInViewModel = koinViewModel<SignInViewModel>(),
) {
    val loginState by loginViewModel.state.collectAsState()
    Log.d("LoginState", loginState.toString())

    LaunchedEffect(Unit) {
        launchCredManBottomSheet(application) { result ->
            loginViewModel.onEvent(SignInEvent.SignInWithGoogle(result, openAndPopUp))
        }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(48.dp))

        AuthTextField(
            value = loginState.email,
            onTextChange = {
                loginViewModel.onEvent(SignInEvent.EditEmail(it))
//                loginViewModel.validateEmail(it)
            },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.email)) },
            type = KeyboardType.Email,
            action = ImeAction.Next,
            isError = loginState.errorState.emailErrorState.hasError,
            errorText = stringResource(loginState.errorState.emailErrorState.errorMessageStringResource),
        )

        AuthTextField(
            value = loginState.password,
            onTextChange = {
                loginViewModel.onEvent(SignInEvent.EditPassword(it))
//                loginViewModel.validatePassword(it)
            },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.password)) },
            type = KeyboardType.Password,
            action = ImeAction.Done,
            onKeyboardDone = { loginViewModel.onEvent(SignInEvent.SignIn(openAndPopUp)) },
            isError = loginState.errorState.passwordErrorState.hasError,
            errorText = stringResource(loginState.errorState.passwordErrorState.errorMessageStringResource),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isConnected(application)) {
                    loginViewModel.onEvent(SignInEvent.SignIn(openAndPopUp))
                } else {
                    Toast.makeText(application, "No internet connection", Toast.LENGTH_LONG)
                        .show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            shape = RoundedCornerShape(15.dp),
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
        ) {
            Text(
                text = "Login",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Don't have an account? ",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
            )
            Text(
                text = AnnotatedString("Sign Up"),
                style =
                    MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                modifier = Modifier.clickable { onNavigateToSignUp() },
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AuthenticationButton(buttonText = R.string.sign_in_with_google) { credential ->
            loginViewModel.onEvent(SignInEvent.SignInWithGoogle(credential, openAndPopUp))
        }
    }
}
