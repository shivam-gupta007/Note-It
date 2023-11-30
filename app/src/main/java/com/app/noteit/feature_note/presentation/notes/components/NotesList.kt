package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.notes.NotesEvent
import com.app.noteit.feature_note.presentation.notes.NotesViewModel
import com.app.noteit.feature_note.presentation.util.Screen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotesList(
    innerPadding: PaddingValues,
    noteList: List<Note>,
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    val allowedDismissDirections = setOf(DismissDirection.EndToStart)

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(all = 8.dp),
    ) {
        items(
            items = noteList,
            key = { it.id!! },
        ) { note ->

            val dismissState = rememberDismissState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                    }
                    true
                }
            )

            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = Dp(1f)),
                directions = allowedDismissDirections,
                background = {  },
                dismissContent = {
                    NoteCard(
                        modifier = Modifier
                            .animateItemPlacement(tween(durationMillis = 250)),
                        note = note,
                        onClick = {
                            val route = if (note.isProtected) {
                                Screen.AuthenticationScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                            } else {
                                Screen.AddEditNotesScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
                            }

                            navController.navigate(route)
                        }
                    )
                }
            )

        }
    }

}
