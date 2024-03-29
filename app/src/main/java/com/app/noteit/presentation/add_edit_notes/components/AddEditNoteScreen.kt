package com.app.noteit.presentation.add_edit_notes.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.noteit.data.data_source.preferences.PasscodeDataStore
import com.app.noteit.presentation.add_edit_notes.AddEditNoteEvent
import com.app.noteit.presentation.add_edit_notes.AddEditNoteState
import com.app.noteit.presentation.add_edit_notes.AddEditNotesViewModel
import com.app.noteit.presentation.util.UrlsIdentifier
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.BrownColor
import com.app.noteit.utils.shareNote
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddEditNoteScreen(
    noteColor: Int,
    onLockNoteClicked : () -> Unit,
    onNoteSaved: () -> Unit,
    onNoteUnsaved: () -> Unit
) {
    val viewModel: AddEditNotesViewModel = hiltViewModel()
    val note = viewModel.addEditNoteState.value
    val defaultBackgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val selectedNoteBackgroundColor = Color(if (noteColor != -1) noteColor else defaultBackgroundColor)
    var showColorPicker by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dataStore = PasscodeDataStore(context = context)
    val passcode = dataStore.getPin.collectAsState(initial = "").value

    val coroutineScope = rememberCoroutineScope()
    val animateNoteBackground = remember {
        Animatable(selectedNoteBackgroundColor)
    }
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditNotesViewModel.UiEvent.ShowSnackBar -> {
                    snackBarHostState.showSnackbar(message = event.message)
                }

                is AddEditNotesViewModel.UiEvent.SaveNote -> {
                    onNoteSaved()
                }

                is AddEditNotesViewModel.UiEvent.SaveNoteFailure ->{
                    onNoteUnsaved()
                }
            }
        }
    }

    BackHandler {
        viewModel.onEvent(AddEditNoteEvent.SaveNote)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            AddEditScreenTopAppBar(
                backgroundColor = animateNoteBackground.value,
                onBackClicked = {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                },
                pinNote = { isPinned ->
                    viewModel.onEvent(AddEditNoteEvent.PinNote(value = isPinned))
                },
                lockNote = { isLocked ->
                    if (passcode.isEmpty()) {
                        onLockNoteClicked()
                    } else {
                        viewModel.onEvent(AddEditNoteEvent.LockNote(value = isLocked))
                    }
                },
                onColorPickerClicked = { showColorPicker = !showColorPicker },
                shareNote = {
                    val noteContent = prepareNoteContentForSharing(note)
                    context.shareNote(noteContent)
                },
                addEditNoteState = note
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(animateNoteBackground.value)
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
        ) {
            AnimatedVisibility (showColorPicker) {
                NoteColorPicker(
                    modifier = Modifier.animateEnterExit(
                        enter = slideInVertically(),
                        exit = slideOutVertically()
                    ),
                    noteColor = animateNoteBackground.value.toArgb(),
                    onColorPicked = { pickedColor ->
                        coroutineScope.launch {
                            animateNoteBackground.animateTo(
                                targetValue = Color(if(pickedColor != -1) pickedColor else defaultBackgroundColor),
                                animationSpec = tween(durationMillis = 100),
                            )
                        }

                        viewModel.onEvent(AddEditNoteEvent.ChangeNoteColor(color = pickedColor))
                    }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            TransparentTextField(
                text = note.title,
                hint = "Title",
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredTitle(it))
                },
                singleLine = true,
                textStyle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                fontSize = 25.sp,
                textSelectionColor = if (animateNoteBackground.value == BlueColor) BrownColor else BlueColor,
                noteBackgroundColor = animateNoteBackground.value,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            )

            Spacer(modifier = Modifier.height(16.dp))

            TransparentTextField(
                modifier = Modifier
                    .fillMaxHeight()
                    .clickable { openUrlInBrowser(note.content) },
                text = note.content,
                hint = "Text",
                onValueChange = {
                    viewModel.onEvent(AddEditNoteEvent.EnteredContent(it))
                },
                singleLine = false,
                textStyle = MaterialTheme.typography.bodyMedium,
                fontSize = 16.sp,
                textSelectionColor = if (animateNoteBackground.value == BlueColor) BrownColor else BlueColor,
                noteBackgroundColor = animateNoteBackground.value
            )
        }
    }
}

fun openUrlInBrowser(text: String) {
    val urls = UrlsIdentifier.URL_REGEX_PATTERN.toRegex().findAll(text).map { it.value }.toList()
}

private fun prepareNoteContentForSharing(note: AddEditNoteState): String {
    return "${note.title}\n\n${note.content}"
}



