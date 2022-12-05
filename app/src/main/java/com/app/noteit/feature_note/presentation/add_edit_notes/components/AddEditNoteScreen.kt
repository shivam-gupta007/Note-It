package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.app.noteit.feature_note.data.data_source.preferences.PasscodeDataStore
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNoteEvent
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNotesViewModel
import com.app.noteit.feature_note.presentation.util.Screen
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.BrownColor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    noteColor: Int,
    viewModel: AddEditNotesViewModel = hiltViewModel()
) {
    val titleState = viewModel.noteTitle.value
    val contentState = viewModel.noteContent.value
    val notePinnedState = viewModel.notePinned.value
    val noteProtectedState = viewModel.noteProtected.value

    val scaffoldState = rememberScaffoldState()

    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
        )
    }

    val isNewNote = remember { noteColor == -1 }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStore = PasscodeDataStore(context = context)
    val passcode = dataStore.getPin.collectAsState(initial = "")

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNotesViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
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
                onBackClicked = { navController.navigateUp() },
                onNotePinned = { isNotePinned ->
                    viewModel.onEvent(AddEditNoteEvent.PinnedNote(value = isNotePinned))
                },
                onNoteProtected = { isNoteProtected ->
                    if (passcode.value.isEmpty()) {
                        navController.navigate(route = Screen.AuthenticationScreen.route)
                    }
                    viewModel.onEvent(AddEditNoteEvent.ProtectedNote(value = isNoteProtected))
                },
                onNoteSaved = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                isNotePinned = notePinnedState.isPinned,
                isNoteProtected = noteProtectedState.isProtected
            )
        },
        scaffoldState = scaffoldState
    ) {
        println(it)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(noteBackgroundAnimatable.value)
                .padding(16.dp)
        ) {
            NoteColorPicker(
                onColorPicked = { colorInt ->
                    scope.launch {
                        noteBackgroundAnimatable.animateTo(
                            targetValue = Color(colorInt),
                            animationSpec = tween(
                                durationMillis = 500
                            )
                        )
                    }
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
                textStyle = MaterialTheme.typography.body1,
                fontSize = 25.sp,
                requestFocus = isNewNote,
                textSelectionColor = if (noteBackgroundAnimatable.value == BlueColor) BrownColor else BlueColor,
                noteBackgroundColor = noteBackgroundAnimatable.value
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentTextField(
                modifier = Modifier
                    .fillMaxHeight(),
                text = contentState.text,
                hint = "Text",
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.body2,
                fontSize = 16.sp,
                textSelectionColor = if (noteBackgroundAnimatable.value == BlueColor) BrownColor else BlueColor,
                noteBackgroundColor = noteBackgroundAnimatable.value
            )
        }
    }
}

@Composable
fun NoteColorPicker(
    viewModel: AddEditNotesViewModel = hiltViewModel(),
    onColorPicked: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        items(items = Note.noteColors) { color ->
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
                    .border(
                        width = 2.dp,
                        color = if (viewModel.noteColor.value == colorInt) Color.White else Color.Transparent,
                        shape = CircleShape
                    )
                    .clickable {
                        onColorPicked(colorInt)
                        viewModel.onEvent(AddEditNoteEvent.ChangeColor(color = colorInt))
                    },
            )
        }
    }
}


@Composable
fun AddEditScreenTopAppBar(
    onBackClicked: () -> Unit,
    onNotePinned: (Boolean) -> Unit,
    onNoteProtected: (Boolean) -> Unit,
    onNoteSaved: () -> Unit,
    isNotePinned: Boolean,
    isNoteProtected: Boolean
) {
    TopAppBar(
        title = { Text("") },
        navigationIcon = {
            IconButton(
                onClick = { onBackClicked() }) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back arrow",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        },
        backgroundColor = MaterialTheme.colors.secondary,
        actions = {
            IconButton(
                onClick = { onNoteProtected(!isNoteProtected) }
            ) {
                Icon(
                    imageVector = if (isNoteProtected) Icons.Filled.Lock else Icons.Outlined.LockOpen,
                    contentDescription = "Protect note",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
            IconButton(
                onClick = { onNotePinned(!isNotePinned) }
            ) {
                Icon(
                    imageVector = if (isNotePinned) Icons.Filled.PushPin else Icons.Outlined.PushPin,
                    contentDescription = "Pin note",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
            IconButton(
                onClick = { onNoteSaved() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.TaskAlt,
                    contentDescription = "Save note",
                    tint = MaterialTheme.colors.onSecondary
                )
            }
        }
    )
}

