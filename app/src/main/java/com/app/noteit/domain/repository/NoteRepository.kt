package com.app.noteit.domain.repository

import com.app.noteit.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun fetchNotes(): Flow<List<NoteEntity>>

    suspend fun fetchNoteById(id: Int): NoteEntity?

    suspend fun deleteNote(note: NoteEntity)

    suspend fun insertNote(note: NoteEntity)

    fun findNotesByQuery(searchQuery : String) : Flow<List<NoteEntity>>
}