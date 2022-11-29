package com.app.noteit.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.noteit.ui.theme.*

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val isPinned: Boolean,
    val isProtected: Boolean,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors =
            listOf(DefaultColor, BlueColor, RedOrange, Violet, BabyBlue, RedPink)
    }
}

class InvalidNoteException(message: String) : Exception(message)