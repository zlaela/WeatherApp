package com.example.weatherapp.ui.composable

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
    value: String,
    enabled: Boolean,
    isError: Boolean,
    searchHint: @Composable () -> Unit,
    leadingIcon: @Composable() (() -> Unit)? = null,
    trailingIcon: @Composable() (() -> Unit)? = null,
    onLeadingIconClick: () -> Unit = {},
    onTrailingIconClick: () -> Unit = {},
    onTextFieldChanged: (String) -> Unit = {},
    onKeyboardDone: () -> Unit = {},
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxWidth()
            .testTag(TestTags.SEARCH_FIELD),
        value = value,
        onValueChange = { onTextFieldChanged(it) },
        label = searchHint,
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
