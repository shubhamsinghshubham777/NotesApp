package com.shubham.notesapp.dataSource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shubham.notesapp.models.Note

@Dao
interface NotesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNote(note: Note)

    @Query("DELETE FROM note_table WHERE id=:noteId")
    suspend fun deleteNote(noteId: Int): Int

    @Query("SELECT * FROM note_table WHERE isDone=:isDone ORDER BY id DESC")
    fun getNotes(isDone: Boolean): PagingSource<Int, Note>

    @Query("SELECT * FROM note_table WHERE description LIKE :query")
    fun searchNote(query: String): PagingSource<Int, Note>

    @Query("DELETE FROM note_table")
    suspend fun deleteAllNotes()
}