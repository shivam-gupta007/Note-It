package com.app.noteit.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.noteit.data.model.NoteEntity

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase : RoomDatabase() {
    abstract val noteDao: NoteDao

    companion object {
        const val DATABASE_NAME = "note_db"
    }
}