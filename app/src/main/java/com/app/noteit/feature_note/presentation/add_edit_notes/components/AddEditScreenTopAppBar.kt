package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNoteState

@Composable
fun AddEditScreenTopAppBar(
    backgroundColor: Int,
    onBackClicked: () -> Unit,
    pinNote: (Boolean) -> Unit,
    lockNote: (Boolean) -> Unit,
    onColorPickerClicked: () -> Unit,
    shareNote: () -> Unit,
    addEditNoteState: AddEditNoteState
) {

    val iconTint = Color(Note.noteTextColorsOnDisplay[backgroundColor] ?: MaterialTheme.colorScheme.onBackground.toArgb())
    val showShareNoteIcon = addEditNoteState.title.isNotEmpty()

    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back arrow",
                    tint = iconTint
                )
            }
        },
        backgroundColor = Color(backgroundColor),
        actions = {
            IconButton(
                onClick = { lockNote(!addEditNoteState.isLocked) }
            ) {
                Icon(
                    imageVector = if (addEditNoteState.isLocked) Icons.Filled.Lock else Icons.Outlined.LockOpen,
                    contentDescription = "Locak note",
                    tint = iconTint
                )
            }
            IconButton(
                onClick = { pinNote(!addEditNoteState.isPinned) }
            ) {
                Icon(
                    imageVector = if (addEditNoteState.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = "Pin note",
                    tint = iconTint
                )
            }
            IconButton(
                onClick = { onColorPickerClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Change note color",
                    tint = iconTint
                )
            }

            if(showShareNoteIcon) {
                IconButton(
                    onClick = { shareNote() }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "Share note",
                        tint = iconTint
                    )
                }
            }
        }
    )
}
