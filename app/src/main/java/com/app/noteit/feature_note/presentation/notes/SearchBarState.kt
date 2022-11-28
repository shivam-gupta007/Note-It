package com.app.noteit.feature_note.presentation.notes

sealed class SearchBarState {
    object OPENED : SearchBarState()
    object CLOSED : SearchBarState()
}
