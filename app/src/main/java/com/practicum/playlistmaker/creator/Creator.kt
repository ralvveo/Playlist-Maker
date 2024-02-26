package com.practicum.playlistmaker.creator

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.repository.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.SearchHistoryFunctionsImpl
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.domain.repository.MyCallback
import com.practicum.playlistmaker.domain.repository.SearchHistoryFunctions

object Creator {
    fun provideMediaplayer(callback: MyCallback): MediaplayerRepository{
        return MediaplayerRepositoryImpl(callback)
    }

    fun provideSearchHistoryFunctions(sharedPrefs: SharedPreferences?) : SearchHistoryFunctions{
        return SearchHistoryFunctionsImpl(sharedPrefs)
    }

}