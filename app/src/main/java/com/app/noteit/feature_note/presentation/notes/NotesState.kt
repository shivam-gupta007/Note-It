package com.app.noteit.feature_note.presentation.notes

import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.util.NoteOrder
import com.app.noteit.feature_note.domain.util.OrderType

data class NotesState(
    var notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false,
    val searchBarState: SearchBarState = SearchBarState.CLOSED,
    val searchText: String = ""
)