package com.app.noteit.feature_note.domain.use_case

data class NoteUseCases(
    val getNotes: GetNotes,
    val insertNote: InsertNote,
    val deleteNote: DeleteNote,
    val getNotesById: GetNotesById
)
