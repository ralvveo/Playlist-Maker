package com.practicum.playlistmaker.creator

import android.content.SharedPreferences
import com.practicum.playlistmaker.data.repository.MediaplayerActivityImpl
import com.practicum.playlistmaker.data.repository.SearchHistoryFunctionsImpl
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.repository.MediaplayerActivity
import com.practicum.playlistmaker.domain.repository.SearchHistoryFunctions

object Creator {
    fun provideMediaplayer(binding: ActivityAudioplayerBinding): MediaplayerActivity{
        return MediaplayerActivityImpl(binding)
    }

    fun provideSearchHistoryFunctions(sharedPrefs: SharedPreferences?) : SearchHistoryFunctions{
        return SearchHistoryFunctionsImpl(sharedPrefs)
    }

}