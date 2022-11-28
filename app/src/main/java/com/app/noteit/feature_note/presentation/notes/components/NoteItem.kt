package com.app.noteit.feature_note.presentation.notes.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.app.noteit.ui.theme.DefaultNoteColor

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    cornerRadius: Dp = 10.dp,
    onClick: () -> Unit,
    onLongClick: (Note,Boolean) -> Unit
) {
    var noteLongPressed by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .background(
                if (Color(note.color) == DefaultNoteColor) MaterialTheme.colorScheme.surfaceVariant
                else Color(note.color)
            )
            .combinedClickable(
                enabled = true,
                onClick = { onClick() },
                onLongClick = {
                    noteLongPressed = !noteLongPressed
                    onLongClick(note, noteLongPressed)
                }
            )
            .border(
                border = BorderStroke(
                    width = 2.dp,
                    color = if (noteLongPressed) Color.White else Color.Transparent
                ),
                shape = RoundedCornerShape(cornerRadius)
            )
            .padding(16.dp)
    ) {

        Text(
            text = note.title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        Text(
            text = note.content,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 10,
            lineHeight = 23.sp,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.onSurfaceVariant
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
            pinned = true
        ),
        onClick = {},
        onLongClick = { note, state ->

        }
    )
}