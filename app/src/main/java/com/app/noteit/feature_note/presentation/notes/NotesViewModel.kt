package com.app.noteit.feature_note.presentation.notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.use_case.NoteUseCases
import com.app.noteit.feature_note.domain.util.NoteOrder
import com.app.noteit.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val notesUseCases: NoteUseCases
) : ViewModel() {

    private var recentlyDeletedNote: Note? = null

    private val _state = mutableStateOf(NotesState())
    val state: State<NotesState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending)) //Getting notes by default
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.Order -> {
                if (state.value.noteOrder::class == event.noteOrder::class &&
                    state.value.noteOrder.orderType == event.noteOrder.orderType
                ) {
                    return
                }
                getNotes(noteOrder = event.noteOrder)
            }
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesUseCases.deleteNote(event.note)
                    recentlyDeletedNote = event.note
                    _eventFlow.emit(UiEvent.ShowSnackbar(message = "${event.note.title} deleted"))
                }
            }
            is NotesEvent.RestoreNote -> {
                viewModelScope.launch {
                    notesUseCases.insertNote(recentlyDeletedNote ?: return@launch)
                    recentlyDeletedNote = null
                }
            }
            is NotesEvent.ToggleOrderSection -> {
                _state.value = _state.value.copy(
                    isOrderSectionVisible = !state.value.isOrderSectionVisible
                )
            }

            is NotesEvent.GetAllNotes -> {
                getNotes(noteOrder = NoteOrder.Date(OrderType.Descending))
            }

            is NotesEvent.SearchNotes -> {
                searchNote(
                    searchText = event.searchText,
                    noteOrder = NoteOrder.Date(OrderType.Descending)
                )
            }

            is NotesEvent.UpdateSearchBarState -> {
                _state.value = _state.value.copy(
                    searchBarState = event.state
                )
            }

            is NotesEvent.UpdateSearchText -> {
                _state.value = _state.value.copy(
                    searchText = event.value
                )
            }

        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = notesUseCases.getNotes(noteOrder)
            .onEach { notes ->
                _state.value = state.value.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )

                _state.value = _state.value.copy(
                    isNotesListEmpty = notes.isEmpty()
                )
            }
            .launchIn(viewModelScope)
    }

    private fun searchNote(searchText: String, noteOrder: NoteOrder) {
        val searchedNoteList = state.value.notes.filter { note ->
            note.title.contains(searchText) || note.content.contains(searchText)
        }

        _state.value = state.value.copy(
            notes = searchedNoteList,
            noteOrder = noteOrder
        )

    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}

