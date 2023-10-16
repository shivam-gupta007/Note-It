package com.app.noteit.feature_note.presentation.add_edit_notes.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.noteit.feature_note.domain.model.Note

@Composable
fun NoteColorPicker(
    itemSize: Dp = 35.dp,
    shape : Shape = CircleShape,
    onColorPicked: (Int) -> Unit
) {
    val defaultColor = MaterialTheme.colorScheme.onBackground
    var pickedColor by remember { mutableStateOf(defaultColor) }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = Note.noteColors) { itemColor ->
            val currentItemColor = Color(itemColor)
            val selectedColor = if (pickedColor == currentItemColor) defaultColor else currentItemColor

            Box(
                modifier = Modifier
                    .size(itemSize)
                    .clip(shape)
                    .background(Color(itemColor))
                    .clickable {
                        onColorPicked(itemColor)
                        pickedColor = currentItemColor
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
