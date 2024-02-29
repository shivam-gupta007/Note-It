package com.app.noteit

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.app.noteit.data.data_source.local.NoteDao
import com.app.noteit.data.data_source.local.NoteDatabase
import com.app.noteit.data.model.NoteEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesDaoTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var notesDao: NoteDao

    @Before
    fun setup(){
        noteDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NoteDatabase::class.java
        ).allowMainThreadQueries().build()

        notesDao = noteDatabase.noteDao
    }

    @Test
    fun insertNote_expectedSingleNote() = runBlocking{
        val note = NoteEntity(
            id = 0,
            title = "This is a test note",
            content = "This is the test note content",
            timestamp = 1000L,
            backgroundColor = 112,
            isPinned = false,
            isProtected = false
        )

        notesDao.insertNote(note)

        val result = notesDao.getNotes().firstOrNull()

        Assert.assertEquals(1, result?.size)
        Assert.assertEquals("This is a test note", result?.get(0)?.title ?: "")
    }

    @Test
    fun deleteNote_expectedNoteDeleted() = runBlocking {
        val note = NoteEntity(
            id = 0,
            title = "This is a test note",
            content = "This is the test note content",
            timestamp = 1000L,
            backgroundColor = 112,
            isPinned = false,
            isProtected = false
        )

        notesDao.insertNote(note)
        notesDao.deleteNote(note)
        val result = notesDao.getNotesById(id = 0)

        Assert.assertNull(result)
    }

    @Test
    fun searchNote_expectedSearchedNote() = runBlocking {
        val note = NoteEntity(
            id = 0,
            title = "This is a sample note",
            content = "This is the note content",
            timestamp = 1000L,
            backgroundColor = 100,
            isPinned = false,
            isProtected = false
        )

        notesDao.insertNote(note)
        val result = notesDao.findNotesByQuery(searchQuery = "sample").firstOrNull()
        Assert.assertNotNull(result)
    }

    @After
    fun tearDown(){
        noteDatabase.close()
    }
}