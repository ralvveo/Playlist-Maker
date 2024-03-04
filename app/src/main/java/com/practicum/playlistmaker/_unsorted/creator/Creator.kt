package com.practicum.playlistmaker._unsorted.creator

import android.content.SharedPreferences
import com.practicum.playlistmaker._unsorted_data.repository.MediaplayerRepositoryImpl
import com.practicum.playlistmaker._unsorted_data.repository.SearchHistoryFunctionsImpl
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker._unsorted_domain.repository.MediaplayerRepository
import com.practicum.playlistmaker._unsorted_domain.repository.MyCallback
import com.practicum.playlistmaker._unsorted_domain.repository.SearchHistoryFunctions

object Creator {
    fun provideMediaplayer(callback: MyCallback): MediaplayerRepository{
        return MediaplayerRepositoryImpl(callback)
    }

    fun provideSearchHistoryFunctions(sharedPrefs: SharedPreferences?) : SearchHistoryFunctions{
        return SearchHistoryFunctionsImpl(sharedPrefs)
    }

}