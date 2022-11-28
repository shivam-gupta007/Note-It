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
    val pinned: Boolean,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors =
            listOf(DefaultNoteColor, LightBlue, RedOrange, LightGreen, Violet, BabyBlue, RedPink, Blue)
    }
}

class InvalidNoteException(message: String) : Exception(message)