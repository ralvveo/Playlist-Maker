package com.practicum.playlistmaker.di

import androidx.room.Room
import com.practicum.playlistmaker.favourites.data.db.Database
import com.practicum.playlistmaker.search.data.network.ItunesSearchApi
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single<ItunesSearchApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesSearchApi::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), Database::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}