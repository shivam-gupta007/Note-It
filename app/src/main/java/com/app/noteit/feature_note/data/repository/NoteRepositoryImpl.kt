package com.app.noteit.feature_note.data.repository

import com.app.noteit.feature_note.data.data_source.local.NoteDao
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun fetchNotes(): Flow<List<Note>> {
        return noteDao.getNotes()
    }

    override suspend fun fetchNoteById(id: Int): Note {
        return noteDao.getNotesById(id)
    }

    override suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }

    override suspend fun insertNote(note: Note) {
        noteDao.insertNote(note)
    }
}