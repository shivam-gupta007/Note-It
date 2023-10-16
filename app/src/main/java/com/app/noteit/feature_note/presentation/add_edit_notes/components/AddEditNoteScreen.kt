package com.app.noteit.feature_note.presentation.add_edit_notes.components

import androidx.compose.animation.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import com.app.noteit.feature_note.data.data_source.preferences.PasscodeDataStore
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNoteEvent
import com.app.noteit.feature_note.presentation.add_edit_notes.AddEditNotesViewModel
import com.app.noteit.feature_note.presentation.util.Screen
import com.app.noteit.feature_note.presentation.util.UrlsIdentifier
import com.app.noteit.ui.theme.BlueColor
import com.app.noteit.ui.theme.BrownColor
import kotlinx.coroutines.flow.collectLatest


@Composable
fun AddEditNoteScreen(
    navController: NavHostController,
    noteColor: Int,
    viewModel: AddEditNotesViewModel = hiltViewModel()
) {

    val note = viewModel.noteState.value
    val scaffoldState = rememberScaffoldState()
    val defaultBackgroundColor = MaterialTheme.colorScheme.background.toArgb()
    val noteBackgroundAnimatable = remember {
        Animatable(
            Color(
                if (noteColor != -1) {
                    noteColor
                } else {
                    //viewModel.onEvent(AddEditNoteEvent.ChangeColor(color = defaultBackgroundColor))
                    //note.backgroundColor
                    defaultBackgroundColor
                }
            )
        )
    }

    /*val noteBackgroundAnimatable = remember {
        Color(if (noteColor != -1) noteColor else viewModel.noteColor.value)
    }*/


    var showColorPicker by remember { mutableStateOf(false) }
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
                    navController.popBackStack()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AddEditScreenTopAppBar(
                onBackClicked = { navController.popBackStack() },
                onNotePinned = { isNotePinned ->
                    viewModel.onEvent(AddEditNoteEvent.PinNote(value = isNotePinned))
                },
                onNoteProtected = { isNoteProtected ->
                    if (passcode.value.isEmpty()) {
                        navController.navigate(route = Screen.AuthenticationScreen.route)
                    }
                    viewModel.onEvent(AddEditNoteEvent.LockNote(value = isNoteProtected))
                },
                onNoteSaved = { viewModel.onEvent(AddEditNoteEvent.SaveNote) },
                onColorPickerClicked = {
                    showColorPicker = !showColorPicker
                },
                onShareNoteClicked = {},
                note = note
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
            if (showColorPicker) {
                NoteColorPicker(
                    onColorPicked = { colorInt ->
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
                textSelectionColor = if (noteBackgroundAnimatable.value == BlueColor) BrownColor else BlueColor,
                noteBackgroundColor = noteBackgroundAnimatable.value,
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
                textSelectionColor = if (noteBackgroundAnimatable.value == BlueColor) BrownColor else BlueColor,
                noteBackgroundColor = noteBackgroundAnimatable.value
            )
        }
    }
}

fun openUrlInBrowser(text: String) {
    val urls = UrlsIdentifier.URL_REGEX_PATTERN.toRegex().findAll(text).map { it.value }.toList()
}



