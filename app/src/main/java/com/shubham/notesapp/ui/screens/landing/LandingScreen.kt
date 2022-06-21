package com.shubham.notesapp.ui.screens.landing

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.shubham.notesapp.R
import com.shubham.notesapp.ui.screens.landing.components.NoteDialog
import com.shubham.notesapp.ui.screens.landing.components.NotesList
import com.shubham.notesapp.ui.theme.NotesAppTheme
import com.shubham.notesapp.viewmodels.LandingPageViewModel
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandingScreen(
    landingPageViewModel: LandingPageViewModel = get()
) {
    var isNoteDialogShown by remember { mutableStateOf(false) }
    var isDeleteAllDialogShown by remember { mutableStateOf(false) }
    val currentNote = landingPageViewModel.currentNote

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text(text = "Your TODOs") },
                modifier = Modifier.statusBarsPadding(),
                actions = {
                    IconButton(onClick = {
                        isDeleteAllDialogShown = true
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    isNoteDialogShown = true
                },
                modifier = Modifier.navigationBarsPadding()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) { bodyPadding ->
        Column(
            modifier = Modifier
                .padding(bodyPadding),
        ) {
            NotesList(
                headerTitle = "Pending",
                noteListFlow = landingPageViewModel.pendingNotesFlow,
                onNoteCheckChanged = { updatedNote ->
                    landingPageViewModel.upsertNote(updatedNote)
                },
                onNoteClicked = { note ->
                    landingPageViewModel.setNote(
                        note = note,
                        onComplete = {
                            isNoteDialogShown = true
                        }
                    )
                }
            )
            NotesList(
                headerTitle = "Done",
                noteListFlow = landingPageViewModel.doneNotesFlow,
                onNoteCheckChanged = { updatedNote ->
                    landingPageViewModel.upsertNote(updatedNote)
                },
                onNoteClicked = { note ->
                    landingPageViewModel.setNote(
                        note = note,
                        onComplete = {
                            isNoteDialogShown = true
                        }
                    )
                },
                applyFabPadding = true
            )
        }
        if (isNoteDialogShown) {
            NoteDialog(
                note = currentNote,
                onDismiss = {
                    landingPageViewModel.resetAll(
                        onComplete = { isNoteDialogShown = false }
                    )
                },
                onConfirm = { updatedNote ->
                    landingPageViewModel.upsertNote(updatedNote)
                },
                onDeleteButtonPressed = { noteId ->
                    noteId?.let { nnNoteId ->
                        landingPageViewModel.deleteNote(
                            noteId = nnNoteId,
                            onComplete = {
                                landingPageViewModel.resetAll(
                                    onComplete = {
                                        isNoteDialogShown = false
                                    }
                                )
                            },
                        )
                    }
                }
            )
        }
        if (isDeleteAllDialogShown) {
            AlertDialog(
                onDismissRequest = {
                    isDeleteAllDialogShown = false
                },
                confirmButton = {
                    FilledTonalButton(onClick = {
                        landingPageViewModel.deleteAllNotes()
                        isDeleteAllDialogShown = false
                    }) {
                        Text(text = stringResource(R.string.dialog_delete_btn_txt))
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        isDeleteAllDialogShown = false
                    }) {
                        Text(text = stringResource(R.string.dialog_cancel_btn_txt))
                    }
                },
                title = { Text(text = stringResource(R.string.dialog_title)) },
                text = { Text(text = stringResource(R.string.dialog_body_txt)) },
            )
        }
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun LandingScreenPreview() {
    NotesAppTheme {
        LandingScreen()
    }
}