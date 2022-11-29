package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.text.TextStyle
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.feature_note.presentation.notes.NotesEvent
import com.app.noteit.feature_note.presentation.notes.NotesViewModel
import com.app.noteit.feature_note.presentation.notes.SearchBarState
import com.app.noteit.feature_note.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalFoundationApi::class
)
@Composable
fun NotesScreen(
    navController: NavController, viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val searchText = state.searchText
    val searchBarState = state.searchBarState

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val lazyVerticalStaggeredGridState = rememberLazyStaggeredGridState()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NotesViewModel.UiEvent.ShowSnackbar -> {
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = "Undo",
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(NotesEvent.RestoreNote)
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MainTopAppBar(searchBarState = searchBarState,
                searchText = searchText,
                onTextChanged = { text ->
                    viewModel.onEvent(NotesEvent.UpdateSearchText(text))
                    viewModel.onEvent(NotesEvent.SearchNotes(text))

                    if (text.isEmpty()) {
                        viewModel.onEvent(NotesEvent.GetAllNotes)
                    }
                },
                onCloseClicked = {
                    viewModel.onEvent(NotesEvent.UpdateSearchText(""))
                    viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                    viewModel.onEvent(NotesEvent.GetAllNotes)
                },
                onSearchClicked = { text ->
                    viewModel.onEvent(NotesEvent.SearchNotes(text))
                },
                onSearchTriggered = {
                    viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.OPENED))
                },
                onMenuTriggered = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                })
        },
        floatingActionButton = {
            NotesScreenFab(onNoteCreated = {
                viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                navController.navigate(Screen.AddEditNotesScreen.route)
            })
        }, backgroundColor = MaterialTheme.colors.background, scaffoldState = scaffoldState

    ) { innerPadding ->

        if (state.searchBarState == SearchBarState.OPENED && state.notes.isEmpty()) {
            EmptyScreen(message = "No matching notes found 😕")
        }

        val pinnedNotesList = state.notes.filter { note ->
            note.isPinned
        }

        val unPinnedNotesList = state.notes.filter { note ->
            !note.isPinned
        }

        val sortedNotesList = pinnedNotesList.plus(unPinnedNotesList)

        NotesList(
            lazyVerticalStaggeredGridState = lazyVerticalStaggeredGridState,
            innerPadding = innerPadding,
            noteList = sortedNotesList,
            navController = navController
        )
    }
}


@Composable
fun MainTopAppBar(
    searchBarState: SearchBarState,
    searchText: String,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit,
    onMenuTriggered: () -> Unit
) {
    when (searchBarState) {
        SearchBarState.CLOSED -> {
            NotesScreenTopAppBar(onSearchClicked = { onSearchTriggered() },
                onMenuClicked = { onMenuTriggered() })
        }

        SearchBarState.OPENED -> {
            SearchAppBar(
                text = searchText,
                onTextChanged = onTextChanged,
                onCloseClicked = onCloseClicked,
                onSearchClicked = onSearchClicked
            )
        }

    }
}


@Composable
fun NotesScreenTopAppBar(
    onSearchClicked: () -> Unit, onMenuClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Notes",
                color = MaterialTheme.colors.onSecondary,
                style = TextStyle(fontSize = MaterialTheme.typography.h5.fontSize)
            )
        },
        backgroundColor = MaterialTheme.colors.secondary,
        actions = {
            IconButton(onClick = { onSearchClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        })
}

@Composable
fun NotesScreenFab(onNoteCreated: () -> Unit) {
    FloatingActionButton(
        shape = CircleShape,
        backgroundColor = MaterialTheme.colors.primary,
        onClick = { onNoteCreated() },
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = MaterialTheme.colors.onPrimary,
        )
    }
}


