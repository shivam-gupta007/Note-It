package com.app.noteit.feature_note.presentation.notes.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.noteit.R
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.ui.theme.NoteItTheme

@Composable
fun NoteCard(
    modifier: Modifier = Modifier,
    note: Note,
    cornerRadius: Dp = 10.dp,
    onClick: () -> Unit
) {
    val defaultBackgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val borderColor = if (note.backgroundColor == -1) MaterialTheme.colorScheme.onBackground else Color.Transparent
    val matchedTextColor = Note.noteTextColors[note.backgroundColor]
    val noteTextColor = if (matchedTextColor != null) Color(matchedTextColor) else MaterialTheme.colorScheme.onBackground
    val noteBackgroundColor = if (note.backgroundColor != -1) Color(note.backgroundColor) else MaterialTheme.colorScheme.background

    Column(
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(cornerRadius))
            .background(noteBackgroundColor)
            .clickable { onClick() }
            .padding(16.dp)
    ) {

        Text(
            text = note.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = noteTextColor
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        if (note.isProtected) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock note",
                tint = MaterialTheme.colorScheme.onBackground
            )
        } else if (note.content.isNotEmpty()) {
            Text(
                modifier = Modifier,
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                color = noteTextColor
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun NoteItemPreview() {
    NoteItTheme {
        NoteCard(
            note = Note(
                title = "Grocery List",
                content = """
                Eggs
                Milk
                Bread
                Rice
                Bananas
            """.trimIndent(),
                timestamp = 100,
                backgroundColor = R.color.teal_200,
                isPinned = false,
                isProtected = false,
            ),
            onClick = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun NoteItemLightPreview() {
    NoteCard(
        note = Note(
            title = "My Title",
            content = """
                Hello, I am android developer
                working 
            """.trimIndent(),
            timestamp = 100, backgroundColor = R.color.teal_200,
            isPinned = true,
            isProtected = true,
        ),
        onClick = {},
    )
}