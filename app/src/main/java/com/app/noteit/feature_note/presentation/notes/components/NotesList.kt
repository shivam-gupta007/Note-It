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
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.notes.NotesEvent
import com.app.noteit.feature_note.presentation.notes.NotesViewModel
import com.app.noteit.feature_note.presentation.notes.SearchBarState
import com.app.noteit.feature_note.presentation.util.Screen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun NotesList(
    innerPadding: PaddingValues,
    noteList: List<Note>,
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(all = 8.dp)
            .animateContentSize(
                animationSpec = snap(
                    delayMillis = 100
                )
            ),
    ) {
        items(
            items = noteList,
            key = { it.id!! },
        ) { note ->

            NoteCard(
                modifier = Modifier
                    .animateItemPlacement(
                        animationSpec = tween(
                            durationMillis = 600
                        )
                    ),
                note = note,
                onClick = {
                    viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                    if (note.isProtected) {
                        navController.navigate(Screen.AuthenticationScreen.route + "?noteId=${note.id}&noteColor=${note.backgroundColor}")
                    } else {
                        navController.navigate(Screen.AddEditNotesScreen.route + "?noteId=${note.id}&noteColor=${note.backgroundColor}")
                    }
                }
            )
        }
    }

}
