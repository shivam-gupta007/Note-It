package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun SearchAppBar(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(height = 60.dp),
        color = MaterialTheme.colors.secondary
    ) {
        Column() {
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
                        color = MaterialTheme.colors.onSecondary,
                    )
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.body1.fontSize
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier.alpha(0.4F),
                        onClick = { onSearchClicked(text) }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                trailingIcon = {
                    IconButton(
                        onClick = {
                            if (text.isNotEmpty()) {
                                onTextChanged("")
                            } else {
                                onCloseClicked()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon",
                            tint = MaterialTheme.colors.onSecondary
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearchClicked(text) }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent,
                    cursorColor = MaterialTheme.colors.onSecondary.copy(alpha = 0.4F)
                )

            )

            /*Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FilterChip(selected = false, onClick = { *//*TODO*//* }) {
                    Icon(imageVector = Icons.Default.Password, contentDescription = "password icon")
                    Text(text = "Passwords")
                }

                FilterChip(selected = false, onClick = { *//*TODO*//* }) {
                    Icon(imageVector = Icons.Default.Note, contentDescription = "note icon")
                    Text(text = "Notes")
                }
            }*/
        }
    }
}