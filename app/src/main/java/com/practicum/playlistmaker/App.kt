package com.practicum.playlistmaker

import android.app.Application
import com.practicum.playlistmaker.di.appModule
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@App)
            modules(appModule, interactorModule, repositoryModule, dataModule)
        }
    }
}