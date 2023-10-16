package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrowseGallery
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable

@Composable
fun EditNoteBottomBar(){
    IconButton(
        onClick = {

        }
    ){
        Icon(
            imageVector = Icons.Default.ColorLens,
            contentDescription = null
        )
    }

    IconButton(
        onClick = {

        }
    ){
        Icon(
            imageVector = Icons.Default.BrowseGallery,
            contentDescription = null
        )
    }

    IconButton(
        onClick = {

        }
    ){
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null
        )
    }


    IconButton(
        onClick = {

        }
    ){
        Icon(
            imageVector = Icons.Default.Share,
            contentDescription = null
        )
    }

}