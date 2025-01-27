package com.example.notes.screens.signUp

import android.app.Application
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
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.R
import com.example.notes.domain.util.AuthenticationButton
import com.example.notes.domain.util.Helper.isConnected
import com.example.notes.screens.common.Ui.AuthTextField
import com.example.notes.ui.theme.NotesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SignUpScreen(
    onNavigateToSignIn: () -> Unit,
    openAndPopUp: () -> Unit,
    application: Application = LocalContext.current.applicationContext as Application,
    modifier: Modifier = Modifier,
    signUpViewModel: SignUpViewModel = koinViewModel()
) {
    val registerState by signUpViewModel.signUpState.collectAsState()

    LaunchedEffect(registerState.errorState.termsErrorState.hasError) {
        Toast.makeText(
            application,
            registerState.errorState.termsErrorState.errorMessageStringResource,
            Toast.LENGTH_LONG
        ).show()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            style = MaterialTheme.typography.displayMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthTextField(
            value = registerState.firstName,
            onTextChange = { signUpViewModel.onEvent(SignUpEvent.EditFirstName(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.first_name)) },
            isError = registerState.errorState.emailErrorState.hasError,
            errorText = stringResource(registerState.errorState.emailErrorState.errorMessageStringResource)

        )
        AuthTextField(
            value = registerState.lastName,
            onTextChange = { signUpViewModel.onEvent(SignUpEvent.EditLastName(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.last_name)) },
            isError = registerState.errorState.emailErrorState.hasError,
            errorText = stringResource(registerState.errorState.emailErrorState.errorMessageStringResource)

        )

        AuthTextField(
            value = registerState.email,
            onTextChange = { signUpViewModel.onEvent(SignUpEvent.EditEmail(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.email)) },
            type = KeyboardType.Email,
            action = ImeAction.Next,
            onKeyboardDone = {},
            isError = registerState.errorState.emailErrorState.hasError,
            errorText = stringResource(registerState.errorState.emailErrorState.errorMessageStringResource)

        )

        AuthTextField(
            value = registerState.password,
            onTextChange = { signUpViewModel.onEvent(SignUpEvent.EditPassword(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.password)) },
            type = KeyboardType.Password,
            action = ImeAction.Next,
            onKeyboardDone = { },
            isError = registerState.errorState.passwordErrorState.hasError,
            errorText = stringResource(registerState.errorState.passwordErrorState.errorMessageStringResource)
        )

        AuthTextField(
            value = registerState.cPassword,
            onTextChange = { signUpViewModel.onEvent(SignUpEvent.EditConfirmPassword(it)) },
            modifier = Modifier.padding(8.dp),
            label = { Text(stringResource(R.string.confirm_password)) },
            type = KeyboardType.Password,
            action = ImeAction.Next,
            onKeyboardDone = { signUpViewModel.onEvent(SignUpEvent.SignUp(openAndPopUp)) },
            isError = registerState.errorState.cPasswordErrorState.hasError,
            errorText = stringResource(registerState.errorState.cPasswordErrorState.errorMessageStringResource)
        )

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = registerState.termsAgreed,
                onCheckedChange = { signUpViewModel.updateTermsAgreed() },
            )
            Text(
                text = "I Read and Accept the terms and condition",
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (isConnected(application))
                    signUpViewModel.onEvent(SignUpEvent.SignUp(openAndPopUp))
                else
                    Toast.makeText(application, "No internet connection", Toast.LENGTH_LONG)
                        .show()
            },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(48.dp),
        ) {
            Text(
                text = "Register", color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Already have an account? ",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp
            )
            Text(
                text = AnnotatedString("Login"),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.clickable { onNavigateToSignIn() }
            )
        }

        AuthenticationButton(buttonText = R.string.sign_up_with_google) { credential ->
            signUpViewModel.onEvent(SignUpEvent.SignUpWithGoogle(credential, openAndPopUp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRegisterScreen() {
    NotesTheme {
        SignUpScreen(
            onNavigateToSignIn = {},
            openAndPopUp = {}
        )
    }
}