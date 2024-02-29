package com.app.noteit.presentation.notes

import com.app.noteit.domain.model.Note

sealed class NotesEvent {
    object GetAllNotes : NotesEvent()
    object RestoreNote : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    data class SearchNotes(val searchText: String) : NotesEvent()
}
