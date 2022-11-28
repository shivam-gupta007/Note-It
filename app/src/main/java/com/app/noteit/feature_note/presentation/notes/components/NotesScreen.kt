package com.app.noteit.feature_note.presentation.notes.components

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.notes.NotesEvent
import com.app.noteit.feature_note.presentation.notes.NotesViewModel
import com.app.noteit.feature_note.presentation.notes.SearchBarState
import com.app.noteit.feature_note.presentation.util.Screen

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
)
@Composable
fun NotesScreen(
    navController: NavController,
    viewModel: NotesViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val searchText = state.searchText
    val searchBarState = state.searchBarState
    val listOfSelectedNotes = state.selectedNotesList

    //val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val lazyVerticalStaggeredGridState = rememberLazyStaggeredGridState()

    Scaffold(
        topBar = {
            MainTopAppBar(
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
                },
                onMenuTriggered = {

                },
                onDeleteTriggered = {
                    listOfSelectedNotes.forEach { note ->
                        viewModel.onEvent(NotesEvent.DeleteNote(note = note))
                    }

                    viewModel.onEvent(NotesEvent.UpdateNoteSelection(value = false))
                }
            )
        },
        floatingActionButton = {
            NotesScreenFab(
                onNoteCreated = {
                    viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                    navController.navigate(Screen.AddEditNotesScreen.route)
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,

        ) { innerPadding ->

        if (state.searchBarState == SearchBarState.OPENED && state.notes.isEmpty()) {
            EmptyScreen(message = "No matching notes found 😕")
        }

        val pinnedNotesList = state.notes.filter { note ->
            note.pinned
        }

        val unPinnedNotesList = state.notes.filter { note ->
            !note.pinned
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesList(
    lazyVerticalStaggeredGridState: LazyStaggeredGridState,
    innerPadding: PaddingValues,
    noteList: List<Note>,
    viewModel: NotesViewModel = hiltViewModel(),
    navController: NavController
) {
    val listOfSelectedNotes by remember { mutableStateOf(value = arrayListOf<Note>()) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(count = 2),
        state = lazyVerticalStaggeredGridState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(all = 8.dp),
        flingBehavior = ScrollableDefaults.flingBehavior()
    ) {
        items(noteList) { note ->
            NoteItem(
                modifier = Modifier.fillMaxWidth(), note = note,
                onClick = {
                    viewModel.onEvent(NotesEvent.UpdateSearchBarState(SearchBarState.CLOSED))
                    navController.navigate(Screen.AddEditNotesScreen.route + "?noteId=${note.id}&noteColor=${note.color}")
                },
                onLongClick = { selectedNote, isNoteSelected ->
                    if (isNoteSelected) {
                        listOfSelectedNotes.add(selectedNote)
                    } else {
                        listOfSelectedNotes.remove(selectedNote)
                    }

                    Log.i("NotesScreen", "listOfSelectedNotes: ${listOfSelectedNotes.size}")

                    viewModel.onEvent(NotesEvent.UpdateNoteSelection(value = listOfSelectedNotes.isNotEmpty()))

                    if (listOfSelectedNotes.isNotEmpty()) {
                        viewModel.onEvent(NotesEvent.UpdateSelectedNotes(selectedNotesList = listOfSelectedNotes))
                    }
                }
            )
        }
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
    onMenuTriggered: () -> Unit,
    onDeleteTriggered: () -> Unit
) {
    when (searchBarState) {
        SearchBarState.CLOSED -> {
            NotesScreenTopAppBar(
                onSearchClicked = { onSearchTriggered() },
                onMenuClicked = { onMenuTriggered() },
                onNoteDeleted = { onDeleteTriggered() }
            )
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
    viewModel: NotesViewModel = hiltViewModel(),
    onSearchClicked: () -> Unit,
    onMenuClicked: () -> Unit,
    onNoteDeleted: () -> Unit
) {
    val isNoteSelected = viewModel.state.value.isNoteSelected

    TopAppBar(
        title = { Text(text = "Notes", color = MaterialTheme.colorScheme.onSurface) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        navigationIcon = {
            IconButton(onClick = { onMenuClicked() }) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "Menu Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    if (isNoteSelected) {
                        onNoteDeleted()
                    } else {
                        onSearchClicked()
                    }
                }
            ) {
                Icon(
                    imageVector = if (isNoteSelected) Icons.Outlined.Delete else Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        })
}

@Composable
fun NotesScreenFab(onNoteCreated: () -> Unit) {
    FloatingActionButton(
        shape = CircleShape,
        containerColor = MaterialTheme.colorScheme.primary,
        onClick = { onNoteCreated() },
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Note",
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}


