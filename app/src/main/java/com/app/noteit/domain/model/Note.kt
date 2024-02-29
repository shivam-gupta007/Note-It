package com.app.noteit.domain.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.app.noteit.ui.theme.BabyBlue
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.DefaultTextColor
import com.app.noteit.ui.theme.RedOrange
import com.app.noteit.ui.theme.RedPink
import com.app.noteit.ui.theme.Violet

data class Note(
    val id: Int? = null,
    val title: String,
    val content: String,
    val timestamp: Long,
    val color: Int,
    val isPinned: Boolean,
    val isProtected: Boolean,
) {
    companion object {
        val noteDisplayColors = listOf(
            BlueColor.toArgb(),
            RedOrange.toArgb(),
            Violet.toArgb(),
            BabyBlue.toArgb(),
            RedPink.toArgb()
        )

        val noteTextColorsOnDisplay = mapOf(
            BlueColor.toArgb() to Color.White.toArgb(),
            RedOrange.toArgb() to DefaultTextColor.toArgb(),
            Violet.toArgb() to DefaultTextColor.toArgb(),
            BabyBlue.toArgb() to DefaultTextColor.toArgb(),
            RedPink.toArgb() to DefaultTextColor.toArgb()
        )
    }
}

fun Note.getDeletedNoteMessage(): String {
    val noteTitle = title.take(40)
    return if (noteTitle.length >= 40) "$noteTitle... deleted" else "$noteTitle deleted"
}

class InvalidNoteException(message: String) : Exception(message)