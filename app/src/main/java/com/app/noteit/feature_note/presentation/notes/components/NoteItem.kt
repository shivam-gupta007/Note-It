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
import androidx.compose.ui.unit.sp
import com.app.noteit.R
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.DefaultColor
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.ui.theme.NoteItTheme
import com.app.noteit.ui.theme.NoteTextColor

@Composable
fun NoteItem(
    modifier: Modifier = Modifier,
    note: Note,
    cornerRadius: Dp = 10.dp,
    onClick: () -> Unit
) {
    val defaultBackgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val borderColor = if (note.backgroundColor == defaultBackgroundColor) MaterialTheme.colorScheme.onBackground else Color.Transparent
    Column(
        modifier = modifier
            .padding(4.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .fillMaxWidth()
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(cornerRadius))
            .background(Color(note.backgroundColor))
            .clickable { onClick() }
            .padding(16.dp)
    ) {

        Text(
            text = note.title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = Color(note.textColor)
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
        )

        if (note.isProtected) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                modifier = Modifier,
                text = note.content,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 10,
                lineHeight = 23.sp,
                overflow = TextOverflow.Ellipsis,
                color = Color(note.textColor)
            )
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
fun NoteItemPreview() {
    NoteItTheme {
        NoteItem(
            note = Note(
                title = "Grocery List", content = """
                Eggs
                Milk
                Bread
                Rice
                Bananas
            """.trimIndent(), timestamp = 100,
                backgroundColor = R.color.teal_200,
                textColor = R.color.black,
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
    NoteItem(
        note = Note(
            title = "My Title", content = """
                Hello, I am android developer
                working 
            """.trimIndent(), timestamp = 100, backgroundColor = R.color.teal_200,
            isPinned = true,
            isProtected = true,
            textColor = R.color.black,
        ),
        onClick = {},
    )
}