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
import com.app.noteit.feature_note.presentation.add_edit_notes.NoteState

@Composable
fun AddEditScreenTopAppBar(
    onBackClicked: () -> Unit,
    onNotePinned: (Boolean) -> Unit,
    onNoteProtected: (Boolean) -> Unit,
    onNoteSaved: () -> Unit,
    onColorPickerClicked: () -> Unit,
    onShareNoteClicked: () -> Unit,
    note: NoteState
) {

    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back arrow",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background,
        actions = {
            IconButton(
                onClick = { onNoteProtected(!note.isProtected) }
            ) {
                Icon(
                    imageVector = if (note.isProtected) Icons.Filled.Lock else Icons.Outlined.LockOpen,
                    contentDescription = "Protect note",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = { onNotePinned(!note.isPinned) }
            ) {
                Icon(
                    imageVector = if (note.isPinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = "Pin note",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            IconButton(
                onClick = { onColorPickerClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Change note color",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

            IconButton(
                onClick = { onShareNoteClicked() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Share,
                    contentDescription = "Share note",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    )
}
