package com.app.noteit.feature_note.presentation.add_edit_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
                        _noteState.value = _noteState.value.copy(
                            title = note.title,
                            content = note.content,
                            isPinned = note.isPinned,
                            isProtected = note.isProtected,
                            backgroundColor = note.backgroundColor
                        )
                    }
                }
            }
        }
    }

    private var currentNoteId: Int? = null

    private val _noteState = mutableStateOf(NoteState())
    val noteState: State<NoteState> get() = _noteState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _noteState.value = _noteState.value.copy(title = event.value)
            }

            is AddEditNoteEvent.EnteredContent -> {
                _noteState.value = _noteState.value.copy(content = event.value)
            }

            is AddEditNoteEvent.ChangeNoteColor -> {
                _noteState.value = _noteState.value.copy(backgroundColor = event.color)
            }

            is AddEditNoteEvent.PinNote -> {
                _noteState.value = _noteState.value.copy(isPinned = event.value)
            }

            is AddEditNoteEvent.LockNote -> {
                _noteState.value = _noteState.value.copy(isProtected = event.value)
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        noteUseCases.insertNote(
                            Note(
                                title = noteState.value.title.trim(),
                                content = noteState.value.content.trim(),
                                timestamp = System.currentTimeMillis(),
                                backgroundColor = noteState.value.backgroundColor,
                                id = currentNoteId,
                                isPinned = noteState.value.isPinned,
                                isProtected = noteState.value.isProtected
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