package com.app.noteit.feature_note.presentation.add_edit_notes

data class NoteTextFieldsState(
    val text: String = "",
    val isPinned: Boolean = false,
    val isProtected: Boolean = false
)
