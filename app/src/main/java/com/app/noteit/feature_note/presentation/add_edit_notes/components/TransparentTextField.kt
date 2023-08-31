package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.TextUnit
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.DefaultColor
import com.app.noteit.ui.theme.NoteTextColor

@Composable
fun TransparentTextField(
    text: String,
    hint: String,
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    textStyle: TextStyle = TextStyle(),
    singleLine: Boolean = false,
    fontSize: TextUnit,
    requestFocus: Boolean = false,
    textSelectionColor: Color,
    noteBackgroundColor: Color,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    val focusRequester = remember { FocusRequester() }

    val customTextSelectionColors = TextSelectionColors(
        handleColor = textSelectionColor,
        backgroundColor = textSelectionColor.copy(alpha = 0.4f)
    )

    val color = if (noteBackgroundColor == DefaultColor || noteBackgroundColor == BlueColor) MaterialTheme.colors.onSecondary else NoteTextColor

    LaunchedEffect(key1 = Unit) {
        if (requestFocus) {
            focusRequester.requestFocus()
        }
    }
    CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
        BasicTextField(
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = text,
            onValueChange = onValueChange,
            singleLine = singleLine,
            cursorBrush = SolidColor(color),
            textStyle = textStyle.copy(
                fontSize = fontSize,
                color = color
            ), keyboardOptions = keyboardOptions
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
                        color = MaterialTheme.colors.onSecondary
                    )
                }
                innerTextField()
            }

        }
    }
}
