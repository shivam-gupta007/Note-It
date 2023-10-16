package com.app.noteit.feature_note.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.noteit.ui.theme.BabyBlue
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.DefaultColor
import com.app.noteit.ui.theme.DefaultTextColor
import com.app.noteit.ui.theme.RedOrange
import com.app.noteit.ui.theme.RedPink
import com.app.noteit.ui.theme.Violet

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val backgroundColor: Int,
    val isPinned: Boolean,
    val isProtected: Boolean,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(
            BlueColor.toArgb(),
            RedOrange.toArgb(),
            Violet.toArgb(),
            BabyBlue.toArgb(),
            RedPink.toArgb(),
            RedPink.toArgb(),
        )

        val noteTextColors = mapOf(
            BlueColor.toArgb() to Color.White.toArgb(),
            RedOrange.toArgb() to DefaultTextColor.toArgb(),
            Violet.toArgb() to DefaultTextColor.toArgb(),
            BabyBlue.toArgb() to DefaultTextColor.toArgb(),
            RedPink.toArgb() to DefaultTextColor.toArgb()
        )
    }
}

class InvalidNoteException(message: String) : Exception(message)