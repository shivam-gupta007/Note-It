package com.app.noteit.presentation.add_edit_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.noteit.data.model.toNoteEntity
import com.app.noteit.domain.model.InvalidNoteException
import com.app.noteit.domain.model.Note
import com.app.noteit.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNotesViewModel @Inject constructor(
    private val repository: NoteRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1) {
                viewModelScope.launch {
                    repository.fetchNoteById(noteId)?.also { note ->
                        currentNoteId = note.id
                        _addEditNoteState.value = _addEditNoteState.value.copy(
                            title = note.title,
                            content = note.content,
                            isPinned = note.isPinned,
                            isLocked = note.isProtected,
                            backgroundColor = note.backgroundColor
                        )
                    }
                }
            }
        }
    }

    private var currentNoteId: Int? = null

    private val _addEditNoteState = mutableStateOf(AddEditNoteState())
    val addEditNoteState: State<AddEditNoteState> get() = _addEditNoteState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddEditNoteEvent) {
        when (event) {
            is AddEditNoteEvent.EnteredTitle -> {
                _addEditNoteState.value = _addEditNoteState.value.copy(title = event.value)
            }

            is AddEditNoteEvent.EnteredContent -> {
                _addEditNoteState.value = _addEditNoteState.value.copy(content = event.value)
            }

            is AddEditNoteEvent.ChangeNoteColor -> {
                _addEditNoteState.value = _addEditNoteState.value.copy(backgroundColor = event.color)
            }

            is AddEditNoteEvent.PinNote -> {
                _addEditNoteState.value = _addEditNoteState.value.copy(isPinned = event.value)
            }

            is AddEditNoteEvent.LockNote -> {
                _addEditNoteState.value = _addEditNoteState.value.copy(isLocked = event.value)
            }

            is AddEditNoteEvent.SaveNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    try {
                        val note = addEditNoteState.value

                        if(note.title.isEmpty() && note.content.isEmpty()){
                            throw InvalidNoteException(message = "Empty note discarded.")
                        }

                        repository.insertNote(
                            Note(
                                title = note.title.trim(),
                                content = note.content.trim(),
                                timestamp = System.currentTimeMillis(),
                                color = note.backgroundColor,
                                id = currentNoteId,
                                isPinned = note.isPinned,
                                isProtected = note.isLocked
                            ).toNoteEntity()
                        )

                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidNoteException) {
                        _eventFlow.emit(UiEvent.SaveNoteFailure)
                        UiEvent.ShowSnackBar(
                            message = e.message ?: "Couldn't save note"
                        )
                    }
                }
            }

        }
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
        object SaveNoteFailure: UiEvent()
    }
}