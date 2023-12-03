package com.app.noteit.feature_note.presentation.notes

import com.app.noteit.feature_note.domain.model.Note

data class NotesState(
    var notes: List<Note> = emptyList(),
    val searchBarState: SearchBarState = SearchBarState.CLOSED,
)