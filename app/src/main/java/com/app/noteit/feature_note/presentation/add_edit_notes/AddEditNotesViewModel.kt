package com.app.noteit.feature_note.presentation.add_edit_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.noteit.feature_note.domain.model.InvalidNoteException
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.use_case.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    noteUseCases.getNotesById(noteId)?.also { note ->
                        currentNoteId = note.id
                        _noteTitle.value = _noteTitle.value.copy(
                            text = note.title
                        )

                        _noteContent.value = _noteContent.value.copy(
                            text = note.content
                        )

                        _noteColor.value = note.color

                        _notePinned.value = _notePinned.value.copy(
                            isPinned = note.isPinned
                        )

                        _noteProtected.value = _noteProtected.value.copy(
                            isProtected = note.isProtected
                        )
                    }
                }

            }

        }
    }

    private var currentNoteId: Int? = null

    private val _noteTitle = mutableStateOf(
        NoteTextFieldsState()
    )

    val noteTitle: State<NoteTextFieldsState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFieldsState()
    )

    val noteContent: State<NoteTextFieldsState> = _noteContent

    private val _notePinned = mutableStateOf(
        NoteTextFieldsState()
    )

    val notePinned: State<NoteTextFieldsState> = _notePinned

    private val _noteProtected = mutableStateOf(
        NoteTextFieldsState()
    )

    val noteProtected: State<NoteTextFieldsState> = _noteProtected

    private val _noteColor = mutableStateOf(Note.noteColors.get(0).toArgb())
    val noteColor: State<Int> = _noteColor

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteTitle.value = _noteTitle.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteContent.value = _noteContent.value.copy(
                    text = event.value
                )
            }

            is AddEditNoteEvent.ChangeColor -> {
                _noteColor.value = event.color
            }

            is AddEditNoteEvent.PinnedNote -> {
                _notePinned.value = notePinned.value.copy(
                    isPinned = event.value
                )
            }

            is AddEditNoteEvent.ProtectedNote -> {
                _noteProtected.value = _noteProtected.value.copy(
                    isProtected = event.value
                )
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.insertNote(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId,
                                isPinned = notePinned.value.isPinned,
                                isProtected = noteProtected.value.isProtected
                            )
                        )

                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        UiEvent.ShowSnackbar(
                            message = e.message ?: "Couldn't save note"
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}