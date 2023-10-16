package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onClearClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 60.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            TextField(
                modifier = modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                value = text,
                onValueChange = { onTextChanged(it) },
                placeholder = {
                    Text(
                        modifier = Modifier.alpha(0.4F),
                        text = "Search your notes",
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier.alpha(0.4F),
                        onClick = { onBackClicked() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back icon",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            onClearClicked()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear Icon",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { /*Handle this case*/ }
                ),
                colors = TextFieldDefaults.colors(
                    cursorColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}