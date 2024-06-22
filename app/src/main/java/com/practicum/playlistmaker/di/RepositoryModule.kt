package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favourites.data.converters.TrackDbConverter
import com.practicum.playlistmaker.favourites.data.repository.MediaRepositoryImpl
import com.practicum.playlistmaker.favourites.domain.repository.MediaRepository
import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsDbConverter
import com.practicum.playlistmaker.playlists.data.repository.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlists.domain.repository.PlaylistRepository
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
        RetrofitSearcherRepositoryImpl(itunesSearchService = get(), appDatabase = get())
    }

    factory<TrackDbConverter>{
        TrackDbConverter()
    }

    factory<PlaylistsDbConverter>{
        PlaylistsDbConverter()
    }

    factory<MediaRepository>{
        MediaRepositoryImpl(get(), get())
    }

    factory<PlaylistRepository>{
        PlaylistRepositoryImpl(get(), get(), get(), get())
    }
}
