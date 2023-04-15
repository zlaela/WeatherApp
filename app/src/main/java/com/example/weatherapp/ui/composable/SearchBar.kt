package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.TestTags

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    enabled: Boolean,
    isError: Boolean,
    searchHint: String,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    onLeadingIconClick: () -> Unit = {},
    onTrailingIconClick: () -> Unit = {},
    onTextFieldBlank: () -> Unit = {},
    onTextFieldChanged: (String) -> Unit = {},
    onKeyboardDone: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var textFieldText by remember { mutableStateOf("") }

    OutlinedTextField(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxWidth()
            .testTag(TestTags.SEARCH_FIELD),
        value = textFieldText,
        onValueChange = { searchTerm ->
            if (searchTerm.isBlank() || searchTerm.isEmpty()) {
                onTextFieldBlank()
            }
            textFieldText = searchTerm
            onTextFieldChanged(textFieldText)
        },
        label = { Text(text = searchHint) },
        enabled = enabled,
        leadingIcon = {
            leadingIcon?.let { icon ->
                IconButton(
                    onClick = { onLeadingIconClick() },
                    content = { icon() }
                )
            }
        },
        isError = isError,
        trailingIcon = {
            trailingIcon?.let { icon ->
                IconButton(
                    onClick = { onTrailingIconClick() },
                    content = { icon() }
                )
            }
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                // Notify
                onKeyboardDone()
                // Hide keyboard
                keyboardController?.hide()
            }
        )
    )
}
