package com.app.noteit.data.repository

import com.app.noteit.data.data_source.local.NoteDao
import com.app.noteit.data.model.NoteEntity
import com.app.noteit.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao) : NoteRepository {
    override fun fetchNotes(): Flow<List<NoteEntity>> {
        return noteDao.getNotes()
    }

    override suspend fun fetchNoteById(id: Int): NoteEntity {
        return noteDao.getNotesById(id)
    }

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }

    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    override fun findNotesByQuery(searchQuery: String): Flow<List<NoteEntity>> {
        return noteDao.findNotesByQuery(searchQuery)
    }
}