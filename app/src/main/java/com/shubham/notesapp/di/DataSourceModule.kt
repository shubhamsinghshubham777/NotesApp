package com.shubham.notesapp.di

import androidx.room.Room
import com.shubham.notesapp.dataSource.local.NotesDb
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DataSourceModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NotesDb::class.java,
            "notes_db"
        ).build()
    }
    single {
        get<NotesDb>().getNotesDao()
    }
}