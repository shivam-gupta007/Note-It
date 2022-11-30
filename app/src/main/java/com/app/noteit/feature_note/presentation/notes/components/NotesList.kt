package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
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
    lazyVerticalStaggeredGridState: LazyStaggeredGridState,
    innerPadding: PaddingValues,
    noteList: List<Note>,
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(count = 2),
        state = lazyVerticalStaggeredGridState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(all = 8.dp)
            .animateContentSize(
                animationSpec = snap(
                    delayMillis = 100
                )
            ),
        flingBehavior = ScrollableDefaults.flingBehavior()
    ) {
        items(
            items = noteList,
            key = { it.id!! },
        ) { note ->

            val dismissState = rememberDismissState(
                confirmStateChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart || dismissValue == DismissValue.DismissedToEnd) {
                        viewModel.onEvent(NotesEvent.DeleteNote(note = note))
                    }
                    true
                }
            )
            SwipeToDismiss(
                state = dismissState,
                modifier = Modifier.padding(vertical = Dp(1f)),
                directions = setOf(
                    DismissDirection.EndToStart
                )/*,
                dismissThresholds = { direction ->
                    //FractionalThreshold(if (direction == DismissDirection.EndToStart) 0.1f else 0.05f)
                }*/,
                dismissContent = {
                    NoteItem(
                        modifier = Modifier,
                        note = note,
                        onClick = {
                            viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                            navController.navigate(Screen.AddEditNotesScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                        }
                    )
                },
                background = {}
            )
        }
    }
}