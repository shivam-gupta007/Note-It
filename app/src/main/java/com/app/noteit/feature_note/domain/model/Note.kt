package com.app.noteit.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.app.noteit.ui.theme.BabyBlue
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.DefaultColor
import com.app.noteit.ui.theme.RedOrange
import com.app.noteit.ui.theme.RedPink
import com.app.noteit.ui.theme.Violet
import com.app.noteit.ui.theme.WhiteColor

@Entity
data class Note(
    val title: String,
    val content: String,
    val timestamp: Long,
    val backgroundColor: Int,
    val textColor: Int,
    val isPinned: Boolean,
    val isProtected: Boolean,
    @PrimaryKey val id: Int? = null
) {
    companion object {
        val noteColors = listOf(
            //Pair(DefaultColor, WhiteColor),
            Pair(BlueColor, WhiteColor),
            Pair(RedOrange, DefaultColor),
            Pair(Violet, DefaultColor),
            Pair(BabyBlue, DefaultColor),
            Pair(RedPink, DefaultColor)
        )

        /*val noteTextColors = mapOf(

        )*/
    }
}

class InvalidNoteException(message: String) : Exception(message)