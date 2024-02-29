package com.app.noteit.presentation.notes

import com.app.noteit.domain.model.Note

data class NotesState(
    var notes: List<Note> = emptyList(),
    val searchBarState: SearchBarState = SearchBarState.CLOSED,
)