package com.shubham.notesapp.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shubham.notesapp.dataSource.local.NotesDao
import com.shubham.notesapp.models.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LandingPageViewModel(
    private val notesDao: NotesDao
) : ViewModel() {
    var pendingNotesFlow: Flow<PagingData<Note>>? by mutableStateOf(null)
        private set
    var doneNotesFlow: Flow<PagingData<Note>>? by mutableStateOf(null)
        private set
    var currentNote: Note? by mutableStateOf(null)
        private set

    init {
        getNotes(isDone = true)
        getNotes(isDone = false)
    }

    fun setNote(note: Note, onComplete: () -> Unit = {}) {
        currentNote = note
        onComplete()
    }

    private fun getNotes(isDone: Boolean) {
        val result = Pager(
            config = PagingConfig(pageSize = 10),
            initialKey = 0,
            pagingSourceFactory = { notesDao.getNotes(isDone) }
        ).flow

        if (isDone) {
            doneNotesFlow = result
        } else {
            pendingNotesFlow = result
        }
    }

    fun upsertNote(note: Note, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            notesDao.upsertNote(note = note)
            onComplete()
        }
    }

    fun deleteNote(noteId: Int, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val result = notesDao.deleteNote(noteId = noteId)
            if (result != 0) {
                onComplete()
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            notesDao.deleteAllNotes()
        }
    }

    fun resetAll(onComplete: () -> Unit = {}) {
        currentNote = null
        onComplete()
    }
}