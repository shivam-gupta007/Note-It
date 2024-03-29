package com.app.noteit.presentation.add_edit_notes

data class AddEditNoteState(
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val isLocked: Boolean = false,
    val backgroundColor: Int = -1
)
