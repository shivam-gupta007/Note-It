package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit

@Composable
fun TransparentTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    fontSize: TextUnit,
    requestFocus: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        if(requestFocus) {
            focusRequester.requestFocus()
        }
    }

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        value = text,
        onValueChange = onValueChange,
        singleLine = singleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        textStyle = textStyle.copy(fontSize = fontSize, color = MaterialTheme.colorScheme.onSurface),
    ) { innerTextField ->
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            if (text.isBlank()) {
                Text(
                    modifier = Modifier.alpha(alpha = 0.5F),
                    text = hint,
                    style = textStyle,
                    fontSize = fontSize,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            innerTextField()
        }

    }
}
