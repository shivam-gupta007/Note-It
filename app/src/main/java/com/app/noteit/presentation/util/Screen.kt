package com.app.noteit.presentation.util

sealed class Screen(val route: String) {
    object NotesScreen : Screen(route = "notes_screen")
    object AddEditNotesScreen : Screen(route = "add_edit_notes_screen")
    object AuthenticationScreen : Screen(route = "authentication_screen")
}