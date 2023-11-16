package com.app.noteit.feature_note.presentation.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.noteit.feature_note.data.model.toNoteEntity
import com.app.noteit.feature_note.data.model.toNoteList
import com.app.noteit.feature_note.domain.model.Note
import com.app.noteit.feature_note.domain.model.getDeletedNoteMessage
import com.app.noteit.feature_note.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel @Inject constructor(
    private val repository : NoteRepository
) : ViewModel() {

    private var recentlyDeletedNote: Note? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _notesFlow = MutableStateFlow<List<Note>>(emptyList())
    val notesFlow = _notesFlow.asStateFlow()

    private var getNotesJob: Job? = null

    init {
        getNotes()
    }

    fun onEvent(event: NotesEvent) {
        when (event) {
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val noteEntity = event.note.toNoteEntity()
                    recentlyDeletedNote = event.note
                    repository.deleteNote(noteEntity)
                    _eventFlow.emit(UiEvent.ShowSnackBar(message = event.note.getDeletedNoteMessage()))
                }
            }

            is NotesEvent.RestoreNote -> {
                viewModelScope.launch(Dispatchers.IO) {
                    val noteEntity = recentlyDeletedNote?.toNoteEntity()
                    recentlyDeletedNote = null
                    noteEntity?.let {
                        repository.insertNote(note = it)
                    }
                }
            }

            is NotesEvent.GetAllNotes -> {
                getNotes()
            }

            is NotesEvent.SearchNotes -> {
                searchNote(event.searchText)
            }
        }
    }

    private fun getNotes() {
        getNotesJob?.cancel()
        getNotesJob = repository.fetchNotes()
            .onEach { notes ->
                _notesFlow.emit(notes.toNoteList())
            }.launchIn(viewModelScope)
    }

    private fun searchNote(searchText: String) {
        getNotesJob?.cancel()
        getNotesJob = repository.findNotesByQuery(searchText)
            .onEach { notes ->
                _notesFlow.emit(notes.toNoteList())
            }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackBar(val message: String) : UiEvent()
    }
}

