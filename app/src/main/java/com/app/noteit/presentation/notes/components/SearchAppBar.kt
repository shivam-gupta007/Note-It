package com.app.noteit.presentation.notes.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
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
     text: String, onTextChanged: (String) -> Unit, onClearClicked: () -> Unit, onBackClicked: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester.requestFocus()
    }

    TextField(modifier = Modifier.fillMaxWidth().height(height = 60.dp).focusRequester(focusRequester), value = text, onValueChange = { onTextChanged(it) }, placeholder = {
        Text(
            modifier = Modifier.alpha(0.6F), text = "Search your notes"
        )
    }, textStyle = TextStyle(
        fontSize = MaterialTheme.typography.bodyMedium.fontSize
    ), singleLine = true, leadingIcon = {
        IconButton(onClick = { onBackClicked() }) {
            Icon(
                imageVector = Icons.Default.ArrowBack, contentDescription = "Back icon"
            )
        }
    }, trailingIcon = {
        IconButton(onClick = {
            onClearClicked()
        }) {
            Icon(
                imageVector = Icons.Default.Close, contentDescription = "Clear Icon"
            )
        }
    }, keyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Search
    ), keyboardActions = KeyboardActions(onSearch = { /*Handle this case*/ }), colors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
    ))
}
