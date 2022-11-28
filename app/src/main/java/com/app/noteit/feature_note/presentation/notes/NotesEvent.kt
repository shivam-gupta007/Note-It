package com.app.noteit.feature_note.presentation.notes

import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    data class Order(val noteOrder: NoteOrder) : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    object RestoreNote : NotesEvent()
    object ToggleOrderSection : NotesEvent()
    object GetAllNotes : NotesEvent()
    data class UpdateNoteSelection(val value: Boolean) : NotesEvent()
    data class UpdateSelectedNotes(val selectedNotesList: List<Note>) : NotesEvent()
    data class SearchNotes(val searchText: String) : NotesEvent()
    data class UpdateSearchBarState(val state: SearchBarState) : NotesEvent()
    data class UpdateSearchText(val value: String) : NotesEvent()
}
