package com.app.noteit.feature_note.presentation.notes.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.R
import com.app.noteit.feature_note.presentation.notes.NotesEvent
import com.app.noteit.feature_note.presentation.notes.NotesViewModel
import com.app.noteit.feature_note.presentation.notes.SearchBarState
import com.app.noteit.feature_note.presentation.util.Screen
import com.app.noteit.feature_note.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val searchText = state.searchText
    val searchBarState = state.searchBarState
    val notesListStatusState = state.isNotesListEmpty

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState()

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

    ModalNavigationDrawer(
        drawerContent = {
            NavigationDrawerContent(drawerState)
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MainTopAppBar(
                    drawerState = drawerState,
                    searchBarState = searchBarState,
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
                    })
            },
            floatingActionButton = {
                NotesScreenFab(onNoteCreated = {
                    viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                    navController.navigate(Screen.AddEditNotesScreen.route)
                })
            },
            containerColor = MaterialTheme.colorScheme.surface,

            ) { innerPadding ->

            if (state.searchBarState == SearchBarState.OPENED && state.notes.isEmpty()) {
                EmptyScreen(
                    message = stringResource(R.string.no_matching_note_found_error_msg),
                    animationUrl = Constants.NOTE_NOT_FOUND_SCREEN_ANIMATION_URL
                )
            } else if (notesListStatusState) {
                EmptyScreen(
                    message = stringResource(R.string.empty_notes_screen_error_msg),
                    animationUrl = Constants.EMPTY_NOTES_SCREEN_ANIMATION_URL
                )
            }

            val sortedNotes = state.notes.sortedByDescending { it.isPinned }

            NotesList(
                innerPadding = innerPadding,
                noteList = sortedNotes,
                navController = navController
            )
        }
    }
}


@Composable
fun MainTopAppBar(
    searchBarState: SearchBarState,
    searchText: String,
    drawerState: DrawerState,
    onTextChanged: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    when (searchBarState) {
        SearchBarState.CLOSED -> {
            NotesScreenTopAppBar(onSearchClicked = { onSearchTriggered() }, drawerState = drawerState)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreenTopAppBar(
    onSearchClicked: () -> Unit,
    drawerState: DrawerState
) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {
            Text(
                text = "Notes",
                color = MaterialTheme.colorScheme.onSurface,
                style = TextStyle(fontSize = MaterialTheme.typography.titleLarge.fontSize)
            )
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface), actions = {
            IconButton(onClick = { onSearchClicked() }) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
fun NotesScreenFab(onNoteCreated: () -> Unit) {
    FloatingActionButton(
        shape = CircleShape,
        modifier = Modifier.padding(all = 16.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = { onNoteCreated() },
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}


