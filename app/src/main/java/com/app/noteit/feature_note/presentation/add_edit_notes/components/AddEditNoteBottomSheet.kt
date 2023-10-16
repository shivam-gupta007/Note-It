package com.app.noteit.feature_note.presentation.add_edit_notes.components

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.noteit.feature_note.domain.model.EditNoteSheetActionType

@Composable
fun AddEditNoteBottomSheet(
    onColorSelected: (Int) -> Unit,
    onItemClick: (EditNoteSheetActionType) -> Unit
) {
    NoteColorPicker {

    }
}

@Composable
fun BottomSheetItem(
    modifier: Modifier = Modifier,
    item: TextWithIcon,
    onClick: (EditNoteSheetActionType) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(item.type) }
            .padding(horizontal = 8.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
        )

        Text(
            text = item.title,
            fontSize = MaterialTheme.typography.titleMedium.fontSize
        )
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_NO)
fun BottomSheetItemPreview() {
    BottomSheetItem(
        item = TextWithIcon(EditNoteSheetActionType.DeleteNote, "Delete note", Icons.Default.Delete),
        onClick = {}
    )
}
