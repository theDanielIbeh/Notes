package com.example.notes.screens.note

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContent(
    date: String,
    title: String,
    content: String,
    editTitle: (String) -> Unit,
    editContent: (String) -> Unit,
    showDate: Boolean,
    toggleDate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(title.isEmpty()) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .wrapContentSize()
            .padding(horizontal = 16.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { change, _ ->
                        change.consume()
                    },
                    onDragStart = {
                        toggleDate()
                    },
                )
            }
    ) {
        AnimatedVisibility(
            visible = showDate,
            modifier = Modifier.fillMaxWidth(),
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Text(
                text = date,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            )
        }
        BasicTextField(
            value = title,
            onValueChange = { editTitle(it) },
            textStyle = MaterialTheme.typography.headlineLarge.copy(color = MaterialTheme.colorScheme.primary),
            maxLines = 1,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            interactionSource = interactionSource,
            singleLine = true,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = "Title",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                enabled = true,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                container = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
        BasicTextField(
            value = content,
            enabled = title.isNotEmpty(),
            onValueChange = { editContent(it) },
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .padding(0.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = title,
                innerTextField = innerTextField,
                placeholder = {
                    Text(
                        text = "Content",
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                enabled = true,
                singleLine = false,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                container = {},
                contentPadding = PaddingValues(0.dp)
            )
        }
    }
}