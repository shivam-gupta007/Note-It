package com.app.noteit.feature_note.presentation.notes.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.noteit.R
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.DefaultColor
import com.app.noteit.ui.theme.DefaultNoteBgColor
import com.app.noteit.ui.theme.NoteTextColor

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    cornerRadius: Dp = 10.dp,
    onClick: () -> Unit
) {
    val textColor = if (Color(note.color) == DefaultColor || Color(note.color) == BlueColor) MaterialTheme.colors.onSecondary else NoteTextColor

    Column(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .background(
                if (Color(note.color) == DefaultColor) DefaultNoteBgColor
                else Color(note.color)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {

        Text(
            text = note.title,
            style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = textColor
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        Text(
            text = note.content,
            style = MaterialTheme.typography.body1,
            maxLines = 10,
            lineHeight = 23.sp,
            overflow = TextOverflow.Ellipsis,
            color = textColor
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun NoteItemPreview() {
    NoteItem(
        note = Note(
            title = "My Title", content = """
                Hello, I am android developer
                working 
            """.trimIndent(), timestamp = 100, color = R.color.teal_200,
            isPinned = true,
            isProtected = true
        ),
        onClick = {},
    )
}