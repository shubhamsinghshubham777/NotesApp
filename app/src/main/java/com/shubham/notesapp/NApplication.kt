package com.shubham.notesapp

import android.app.Application
import com.shubham.notesapp.di.DataSourceModule
import com.shubham.notesapp.di.ViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class NApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NApplication)
            androidLogger(level = Level.DEBUG)
            modules(DataSourceModule, ViewModelModule)
        }
    }
}