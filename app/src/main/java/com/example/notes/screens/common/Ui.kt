package com.example.notes.screens.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object Ui {
    /**
     * Composable for text fields in authentication screens
     */
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AuthTextField(
        value: String,
        onTextChange: (String) -> Unit,
        label: @Composable () -> Unit,
        type: KeyboardType = KeyboardType.Text,
        action: ImeAction = ImeAction.Next,
        onKeyboardDone: () -> Unit = {},
        isError: Boolean = false,
        errorText: String = "",
        modifier: Modifier = Modifier,
    ) {
        val interactionSource = remember { MutableInteractionSource() }
        val focusRequester = remember { FocusRequester() }

        BasicTextField(
            value = value,
            singleLine = true,
            onValueChange = onTextChange,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = type,
                imeAction = action
            ),
            keyboardActions = KeyboardActions(
                onDone = { onKeyboardDone() },
            ),
            modifier = modifier
                .fillMaxWidth()
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = value,
                innerTextField = innerTextField,
                placeholder = label,
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                contentPadding = PaddingValues(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Gray,
                    disabledTextColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                    disabledIndicatorColor = MaterialTheme.colorScheme.tertiaryContainer,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                supportingText = {
                    if (isError) {
                        Text(
                            text = errorText,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
            )
        }
    }
}