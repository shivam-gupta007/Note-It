package com.app.noteit.feature_note.presentation.util

sealed class Screen(val route: String) {
    object NotesScreen : Screen(route = "notes_screen")
    object AddEditNotesScreen : Screen(route = "add_edit_notes_screen")
}