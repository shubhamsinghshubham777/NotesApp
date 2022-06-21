package com.shubham.notesapp.di

import com.shubham.notesapp.viewmodels.LandingPageViewModel
import org.koin.dsl.module

val ViewModelModule = module {
    single { LandingPageViewModel(notesDao = get()) }
}