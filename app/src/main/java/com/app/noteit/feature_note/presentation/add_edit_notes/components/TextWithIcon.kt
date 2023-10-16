package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.ui.graphics.vector.ImageVector
import com.app.noteit.feature_note.domain.model.EditNoteSheetActionType

data class TextWithIcon(
    val type: EditNoteSheetActionType,
    val title: String,
    val icon: ImageVector,
)
