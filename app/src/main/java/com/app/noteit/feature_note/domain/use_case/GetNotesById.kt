package com.app.noteit.feature_note.domain.use_case

import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.repository.NoteRepository

class GetNotesById(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(id: Int): Note?{
        return repository.fetchNoteById(id)
    }
}