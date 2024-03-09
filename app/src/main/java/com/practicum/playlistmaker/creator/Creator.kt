package com.practicum.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.SearchHistoryFunctionsRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.search.data.repository.RetrofitCallback
import com.practicum.playlistmaker.search.data.repository.RetrofitSearcherRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryFunctionsRepository
import com.practicum.playlistmaker.settings.data.ThemeSwitcherRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.ThemeSwitcherRepository
import com.practicum.playlistmaker.sharing.data.ShareLinksOpenerRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.repository.ShareLinksOpenerRepository

object Creator {
    fun provideMediaplayer(callback: MyCallback): MediaplayerRepository {
        return MediaplayerRepositoryImpl(callback)
    }

    fun provideSearchHistoryFunctions(context: Context): SearchHistoryFunctionsRepository {
        return SearchHistoryFunctionsRepositoryImpl(context)
    }

    fun provideShareLinksOpener(context: Context): ShareLinksOpenerRepository {
        return ShareLinksOpenerRepositoryImpl(context)
    }

    fun provideThemeSwitcher(context: Context): ThemeSwitcherRepository {
        return ThemeSwitcherRepositoryImpl(context)
    }

    fun provideRetrofitSearcher(callback: MyCallback, retrofitCallback: RetrofitCallback): RetrofitSearcherRepository{
        return RetrofitSearcherRepositoryImpl(callback, retrofitCallback)
    }
}