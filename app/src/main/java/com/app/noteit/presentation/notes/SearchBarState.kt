package com.app.noteit.presentation.notes

sealed class SearchBarState {
    object OPENED : SearchBarState()
    object CLOSED : SearchBarState()
}
