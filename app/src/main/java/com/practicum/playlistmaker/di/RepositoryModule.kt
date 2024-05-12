package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.search.data.repository.RetrofitSearcherRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.SearchHistoryFunctionsRepositoryImpl
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryFunctionsRepository
import com.practicum.playlistmaker.settings.data.ThemeSwitcherRepositoryImpl
import com.practicum.playlistmaker.settings.domain.repository.ThemeSwitcherRepository
import com.practicum.playlistmaker.sharing.data.ShareLinksOpenerRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.repository.ShareLinksOpenerRepository
import org.koin.dsl.module

val repositoryModule = module{

    factory <MediaplayerRepository>{ (callback: MyCallback) ->
        MediaplayerRepositoryImpl(callback = callback)
    }

    factory <SearchHistoryFunctionsRepository>{
        SearchHistoryFunctionsRepositoryImpl(context = get())
    }

    factory <ShareLinksOpenerRepository> {
        ShareLinksOpenerRepositoryImpl(context = get())
    }

    factory <ThemeSwitcherRepository>{
        ThemeSwitcherRepositoryImpl(context = get())
    }

    factory <RetrofitSearcherRepository> {
        RetrofitSearcherRepositoryImpl(itunesSearchService = get())
    }
}
