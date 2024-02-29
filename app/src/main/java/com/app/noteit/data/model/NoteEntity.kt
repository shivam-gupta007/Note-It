package com.app.noteit.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_entity")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val backgroundColor: Int,
    val isPinned: Boolean,
    val isProtected: Boolean,
)
