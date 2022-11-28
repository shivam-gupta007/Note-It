package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNoteEvent
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNotesViewModel
import com.app.noteit.feature_note.presentation.util.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNotesViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val notePinnedState = viewModel.notePinned.value

    //val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    val isNewNote = remember { noteColor == -1 }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNotesViewModel.UiEvent.ShowSnackbar -> {
                    //scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }

                is AddEditNotesViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AddEditScreenTopAppBar(
                onBackClicked = { navController.navigate(Screen.NotesScreen.route) },
                onNotePinned = { notePinned ->
                    viewModel.onEvent(AddEditNoteEvent.PinnedNote(value = notePinned))
                },
                onNoteSaved = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                notePinned = notePinnedState.pinned,
            )
        },// scaffoldState = scaffoldState
    ) {
        println(it)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)//.copy(alpha = 0.4F))
                .padding(16.dp)
        ) {
            NoteColors(
                onColorClicked = { colorInt ->
                    scope.launch {
                        noteBackgroundAnimatable.animateTo(
                            targetValue = Color(colorInt),
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        )
                    }
                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(colorInt))
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            TransparentTextField(
                text = titleState.text,
                hint = "Title",
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge,
                fontSize = 25.sp,
                requestFocus = isNewNote
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentTextField(
                modifier = Modifier.fillMaxHeight(),
                text = contentState.text,
                hint = "Text",
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun NoteColors(
    onColorClicked: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Note.noteColors.forEach { color ->
            val colorInt = color.toArgb()

            Box(
                modifier = Modifier
                    .size(30.dp)
                    .shadow(
                        elevation = 10.dp,
                        shape = CircleShape,
                    )
                    .clip(CircleShape)
                    .background(color)
                    /*.border(
                        width = 3.dp,
                        color = if (viewModel.noteColor.value == colorInt) Color.Gray
                        else Color.Transparent,
                        shape = CircleShape
                    )*/
                    .clickable { onColorClicked(colorInt) }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenTopAppBar(
    onBackClicked: () -> Unit,
    onNotePinned: (Boolean) -> Unit,
    onNoteSaved: () -> Unit,
    notePinned: Boolean
) {
    TopAppBar(title = { Text("") },
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back arrow",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        actions = {
            IconButton(
                onClick = { onNotePinned(!notePinned) }
            ) {
                Icon(
                    if (notePinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = "Note pin",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(
                onClick = { onNoteSaved() }
            ) {
                Icon(
                    Icons.Outlined.TaskAlt,
                    contentDescription = "Note saved",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    )
}

@Composable
fun AddEditScreenFab(onNoteSaved: () -> Unit) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = { onNoteSaved() },
        containerColor = MaterialTheme.colorScheme.primary,
    ) {
        Icon(
            imageVector = Icons.Default.Save,
            contentDescription = "Save note",
            tint = MaterialTheme.colorScheme.onPrimary,
        )
    }
}
