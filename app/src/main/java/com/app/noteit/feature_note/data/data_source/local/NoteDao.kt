package com.app.noteit.feature_note.data.data_source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.noteit.feature_note.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_entity ORDER BY id AND isPinned DESC")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_entity WHERE id =:id")
    suspend fun getNotesById(id: Int): NoteEntity

    @Delete
    suspend fun deleteNote(note: NoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM note_entity WHERE title LIKE '%' || :searchQuery || '%' or content LIKE '%' || :searchQuery || '%';")
    fun findNotesByQuery(searchQuery: String) : Flow<List<NoteEntity>>
}