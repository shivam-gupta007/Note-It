package com.app.noteit.feature_note.domain.repository

import com.app.noteit.feature_note.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun fetchNotes(): Flow<List<Note>>

    suspend fun fetchNoteById(id: Int): Note?

    suspend fun deleteNote(note: Note)

    suspend fun insertNote(note: Note)
}