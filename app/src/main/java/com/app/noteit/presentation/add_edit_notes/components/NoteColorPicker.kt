package com.app.noteit.presentation.add_edit_notes.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.noteit.domain.model.Note

@Composable
fun NoteColorPicker(
    modifier: Modifier = Modifier,
    itemSize: Dp = 35.dp,
    shape : Shape = CircleShape,
    noteColor: Int,
    onColorPicked: (Int) -> Unit
) {
    val borderColorSelection = MaterialTheme.colorScheme.onBackground.toArgb()
    var pickedColor by remember {  mutableIntStateOf(noteColor) }

    val defaultNoteBackground = MaterialTheme.colorScheme.background.toArgb()

    val noteBackgroundColors = mutableListOf(defaultNoteBackground)
    noteBackgroundColors.addAll(Note.noteDisplayColors)

    LazyRow(
        modifier = modifier.fillMaxWidth().padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = noteBackgroundColors) { itemColor ->
            val selectedColor = if (pickedColor == itemColor) Color(borderColorSelection) else Color.Transparent

            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(shape)
                    .background(Color(itemColor))
                    .clickable {
                        onColorPicked (if (itemColor == defaultNoteBackground) -1 else itemColor)
                        pickedColor = itemColor
                    }.border(width = 2.dp, color = selectedColor, shape = shape)
                    .padding(all = 4.dp),
            ) {
                    Icon(
                        modifier = Modifier.size(itemSize),
                        imageVector = Icons.Outlined.Check,
                        contentDescription = "Checked",
                        tint = selectedColor
                    )
                            }
        }
    }
}
