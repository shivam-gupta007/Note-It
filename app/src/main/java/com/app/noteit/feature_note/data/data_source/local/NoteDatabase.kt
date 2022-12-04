package com.app.noteit.feature_note.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.noteit.feature_note.data.data_source.local.NoteDao
import com.app.noteit.feature_note.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "note_db"
    }
}