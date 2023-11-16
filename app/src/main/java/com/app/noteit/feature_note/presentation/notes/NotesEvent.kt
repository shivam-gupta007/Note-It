package com.app.noteit.feature_note.presentation.notes

import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.util.NoteOrder

sealed class NotesEvent {
    object GetAllNotes : NotesEvent()
    object RestoreNote : NotesEvent()
    data class DeleteNote(val note: Note) : NotesEvent()
    data class SearchNotes(val searchText: String) : NotesEvent()
}
