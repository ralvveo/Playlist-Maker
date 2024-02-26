package com.practicum.playlistmaker.creator

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.repository.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.data.repository.SearchHistoryFunctionsImpl
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.domain.repository.SearchHistoryFunctions

object Creator {
    fun provideMediaplayer(binding: ActivityAudioplayerBinding): MediaplayerRepository{
        return MediaplayerRepositoryImpl(binding)
    }

    fun provideSearchHistoryFunctions(sharedPrefs: SharedPreferences?) : SearchHistoryFunctions{
        return SearchHistoryFunctionsImpl(sharedPrefs)
    }

}