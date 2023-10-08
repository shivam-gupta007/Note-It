package com.app.noteit.feature_note.presentation.add_edit_notes

import com.app.noteit.feature_note.domain.model.Note

data class NoteState(
    val title: String = "",
    val content: String = "",
    val isPinned: Boolean = false,
    val isProtected: Boolean = false,
    val backgroundColor: Int = -1
)
