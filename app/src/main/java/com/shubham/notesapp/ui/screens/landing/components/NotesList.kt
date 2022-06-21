package com.shubham.notesapp.ui.screens.landing.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.shubham.notesapp.models.Note
import kotlinx.coroutines.flow.Flow

val FabSize = 56.0.dp

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun NotesList(
    headerTitle: String,
    noteListFlow: Flow<PagingData<Note>>?,
    onNoteCheckChanged: (Note) -> Unit,
    onNoteClicked: (Note) -> Unit,
    applyFabPadding: Boolean = false
) {
    val listItems = noteListFlow?.collectAsLazyPagingItems()
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(if (applyFabPadding) 1f else 0.5f)
    ) {
        stickyHeader {
            NoteListHeader(title = headerTitle)
        }
        listItems?.let { nnListItems ->
            items(nnListItems) { note ->
                note?.let { nnNote ->
                    NoteCard(
                        note = nnNote,
                        onCheckedChange = { isChecked ->
                            onNoteCheckChanged(nnNote.copy(isDone = isChecked))
                        },
                        onClick = {
                            onNoteClicked(nnNote)
                        },
                    )
                }
            }
        }
        if (applyFabPadding) {
            item {
                Spacer(
                    modifier = Modifier
                        .padding(bottom = FabSize)
                        .navigationBarsPadding()
                )
            }
        }
    }
}