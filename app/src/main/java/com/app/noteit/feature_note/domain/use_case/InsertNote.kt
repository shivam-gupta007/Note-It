package com.app.noteit.feature_note.domain.use_case

import com.app.noteit.feature_note.domain.model.InvalidNoteException
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.repository.NoteRepository

class InsertNote(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: Note) {
        if (note.title.isNotBlank() || note.content.isNotBlank()) {
            repository.insertNote(note = note)
        } else if (note.title.isBlank()) {
            throw InvalidNoteException("The title of the note can't be empty")
        } else if (note.content.isBlank()) {
            throw InvalidNoteException("The content of the note can't be empty")
        }
    }
}