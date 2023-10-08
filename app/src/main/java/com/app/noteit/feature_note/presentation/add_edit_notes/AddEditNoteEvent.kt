package com.app.noteit.feature_note.presentation.add_edit_notes

sealed class AddEditNoteEvent {
    data class EnteredTitle(val value: String) : AddEditNoteEvent()
    data class EnteredContent(val value: String) : AddEditNoteEvent()
    data class PinNote(val value: Boolean) : AddEditNoteEvent()
    data class LockNote(val value: Boolean) : AddEditNoteEvent()
    data class ChangeNoteColor(val color: Int) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
}
