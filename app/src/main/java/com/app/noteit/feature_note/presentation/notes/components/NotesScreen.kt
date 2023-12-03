package com.app.noteit.feature_note.presentation.notes.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.noteit.R
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.notes.NotesEvent
import com.app.noteit.feature_note.presentation.notes.NotesViewModel
import com.app.noteit.feature_note.presentation.notes.SearchBarState
import com.app.noteit.feature_note.presentation.util.Screen
import com.app.noteit.feature_note.utils.Constants
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun NotesScreen(
    onAddNoteAction: () -> Unit,
    onNoteClicked: (String) -> Unit
) {
    val viewModel: NotesViewModel = hiltViewModel()
    val notes = viewModel.notesFlow.collectAsState().value
    var searchText by remember { mutableStateOf("") }
    var searchBarState by remember { mutableStateOf<SearchBarState>(SearchBarState.CLOSED) }

    val snackBarHostState = remember { SnackbarHostState() }

    fun clearSearchText() {
        searchText = ""
    }

    fun isSearchBarOpened() = (searchBarState == SearchBarState.OPENED)
    fun closeSearchBar() {
        searchBarState = SearchBarState.CLOSED
    }

    val allowedDismissDirections = setOf(DismissDirection.EndToStart)

    BackHandler {
        if (isSearchBarOpened()) {
            closeSearchBar()
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NotesViewModel.UiEvent.ShowSnackBar -> {
                    val result = snackBarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "Undo",
                        duration = SnackbarDuration.Short
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NotesEvent.RestoreNote)
                    }
                }
            }
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            AnimatedVisibility(
                visible = isSearchBarOpened(), enter = expandHorizontally(expandFrom = Alignment.Start), exit = shrinkHorizontally()
            ) {
                SearchAppBar(text = searchText, onTextChanged = { text ->
                    searchText = text

                    viewModel.onEvent(NotesEvent.SearchNotes(text))

                    if (text.isEmpty()) {
                        viewModel.onEvent(NotesEvent.GetAllNotes)
                    }
                }, onClearClicked = {
                    clearSearchText()
                }, onBackClicked = {
                    clearSearchText()
                    closeSearchBar()
                    viewModel.onEvent(NotesEvent.GetAllNotes)
                })

            }

            if (searchBarState == SearchBarState.CLOSED) {
                NotesScreenTopAppBar(onSearchTriggered = { searchBarState = SearchBarState.OPENED })
            }

        },
        floatingActionButton = {
            NotesScreenFab(onAddNote = {
                closeSearchBar()
                onAddNoteAction()
            })
        },
        containerColor = MaterialTheme.colorScheme.surface,
        ) { innerPadding ->

        if (isSearchBarOpened() && notes.isEmpty()) {
            EmptyScreen(
                stringRes = R.string.no_matching_note_found_error_msg, animationUrl = Constants.NOTE_NOT_FOUND_SCREEN_ANIMATION_URL
            )
        } else if(notes.isEmpty()){
            EmptyScreen(
                stringRes = R.string.empty_notes_screen_error_msg, animationUrl = Constants.EMPTY_NOTES_SCREEN_ANIMATION_URL
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(innerPadding).padding(all = 8.dp),
        ) {
            items(
                items = notes,
                key = { it.id!! },
            ) { note ->

                val dismissState = rememberDismissState(confirmValueChange = { dismissValue ->
                    if (dismissValue == DismissValue.DismissedToStart) {
                        viewModel.onEvent(NotesEvent.DeleteNote(note))
                    }
                    true
                })

                SwipeToDismiss(state = dismissState, modifier = Modifier.padding(vertical = Dp(1f)), directions = allowedDismissDirections, background = { }, dismissContent = {
                    NoteCard(
                        modifier = Modifier.animateItemPlacement(tween(durationMillis = 250)),
                        note = note,
                        onClick = {
                        onNoteClicked(getDestinationRoute(note))
                    }
                    )
                })

            }
        }
    }
}


private fun getDestinationRoute(note: Note) : String {
    return if (note.isProtected) {
        Screen.AuthenticationScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
    } else {
        Screen.AddEditNotesScreen.route + "?noteId=${note.id}&noteColor=${note.color}"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreenTopAppBar(
    onSearchTriggered: () -> Unit
) {

    CenterAlignedTopAppBar(title = {
        Text(
            text = "Notes", style = MaterialTheme.typography.headlineSmall
        )
    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background, titleContentColor = MaterialTheme.colorScheme.onBackground
    ), actions = {
        IconButton(onClick = { onSearchTriggered() }) {
            Icon(
                imageVector = Icons.Filled.Search, contentDescription = "Search Icon", tint = MaterialTheme.colorScheme.onSurface
            )
        }
    })
}

@Composable
fun NotesScreenFab(onAddNote: () -> Unit) {
    FloatingActionButton(
        shape = CircleShape,
        modifier = Modifier.padding(all = 16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = { onAddNote() },
    ) {
        Icon(
            imageVector = Icons.Default.Add, contentDescription = "Add Note", tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}


