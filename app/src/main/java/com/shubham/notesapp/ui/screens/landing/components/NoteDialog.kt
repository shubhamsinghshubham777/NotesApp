package com.shubham.notesapp.ui.screens.landing.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shubham.notesapp.R
import com.shubham.notesapp.models.Note
import com.shubham.notesapp.ui.theme.NotesAppTheme

enum class OperationType {
    INSERT, UPDATE
}

@Composable
fun NoteDialog(
    note: Note?,
    onDismiss: () -> Unit,
    onConfirm: (updatedNote: Note) -> Unit,
    onDeleteButtonPressed: (noteId: Int?) -> Unit
) {
    val noteId by remember { mutableStateOf(note?.id) }
    var noteDescription by remember { mutableStateOf(note?.description ?: "") }
    val noteIsDone by remember { mutableStateOf(note?.isDone ?: false) }
    val currentNote by remember(noteId, noteDescription, noteIsDone) {
        mutableStateOf(
            Note(
                id = noteId ?: 0,
                description = noteDescription,
                isDone = noteIsDone
            )
        )
    }

    val operationType: OperationType = note?.let { OperationType.UPDATE } ?: OperationType.INSERT
    val dialogTitle by remember {
        mutableStateOf(
            when (operationType) {
                OperationType.INSERT -> "Add Note"
                OperationType.UPDATE -> "Update Note"
            }
        )
    }

    var isDeleteDialogShown by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            tonalElevation = 8.dp
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .alpha(0.75f),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dialogTitle,
                        style = MaterialTheme.typography.titleLarge,
                    )
                    if (operationType == OperationType.UPDATE) {
                        IconButton(
                            onClick = {
                                isDeleteDialogShown = true
                            }
                        ) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        }
                    }
                }
                TextField(
                    value = noteDescription,
                    onValueChange = { updatedText ->
                        noteDescription = updatedText
                    },
                    placeholder = { Text(text = "e.g. Take dog for a walk...") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        capitalization = KeyboardCapitalization.Sentences,
                        autoCorrect = false
                    ),
                    singleLine = true
                )
                FilledTonalButton(
                    onClick = {
                        onConfirm(currentNote)
                        onDismiss()
                    }, modifier = Modifier.padding(top = 8.dp),
                    enabled = noteDescription.isNotBlank()
                ) {
                    Text(
                        text = when (operationType) {
                            OperationType.INSERT -> "Save"
                            OperationType.UPDATE -> "Update"
                        }
                    )
                }
            }
        }
    }

    if (isDeleteDialogShown) {
        AlertDialog(
            onDismissRequest = {
                isDeleteDialogShown = false
            },
            confirmButton = {
                FilledTonalButton(onClick = {
                    onDeleteButtonPressed(noteId)
                    isDeleteDialogShown = false
                }) {
                    Text(text = stringResource(R.string.dialog_delete_btn_txt))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    isDeleteDialogShown = false
                }) {
                    Text(text = stringResource(R.string.dialog_cancel_btn_txt))
                }
            },
            title = { Text(text = stringResource(R.string.dialog_title)) },
            text = { Text(text = stringResource(R.string.note_dialog_body_txt)) },
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
fun NoteDialogPreview() {

    NotesAppTheme {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(4.dp)
        ) {}
        NoteDialog(
            note = null,
            onDismiss = {},
            onConfirm = {},
            onDeleteButtonPressed = {}
        )
    }
}