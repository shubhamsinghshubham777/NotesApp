package com.shubham.notesapp.dataSource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shubham.notesapp.models.Note

@Database(
    entities = [
        Note::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NotesDb: RoomDatabase() {
    abstract fun getNotesDao(): NotesDao
}