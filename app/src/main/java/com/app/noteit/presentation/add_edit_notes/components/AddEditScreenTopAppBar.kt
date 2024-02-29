package com.app.noteit.presentation.add_edit_notes.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.noteit.domain.model.Note
import com.app.noteit.presentation.add_edit_notes.AddEditNoteState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenTopAppBar(
    backgroundColor: Color,
    onBackClicked: () -> Unit,
    pinNote: (Boolean) -> Unit,
    lockNote: (Boolean) -> Unit,
    onColorPickerClicked: () -> Unit,
    shareNote: () -> Unit,
    addEditNoteState: AddEditNoteState
) {

    val iconTint = Color(Note.noteTextColorsOnDisplay[backgroundColor.toArgb()] ?: MaterialTheme.colorScheme.onBackground.toArgb())
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
        colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor, scrolledContainerColor = backgroundColor),
        actions = {
            IconButton(
                onClick = { lockNote(!addEditNoteState.isLocked) }
            ) {
                Icon(
                    imageVector = if (addEditNoteState.isLocked) Icons.Filled.Lock else Icons.Outlined.LockOpen,
                    contentDescription = "Lock note",
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
