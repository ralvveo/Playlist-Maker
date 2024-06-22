package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.favourites.domain.interactor.MediaInteractor
import com.practicum.playlistmaker.favourites.domain.impl.MediaInteractorImpl
import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.interactor.MediaplayerInteractor
import com.practicum.playlistmaker.player.domain.impl.MediaplayerInteractorImpl
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.playlists.domain.impl.PlaylistsInteractorImpl
import com.practicum.playlistmaker.playlists.domain.interactor.PlaylistsInteractor
import com.practicum.playlistmaker.search.domain.RetrofitSearcherInteractor
import com.practicum.playlistmaker.search.domain.SearchHistoryFunctionsInteractor
import com.practicum.playlistmaker.search.domain.interactor.RetrofitSearcherInteractorImpl
import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryFunctionsInteractorImpl
import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.interactor.ThemeSwitcherInteractorImpl
import com.practicum.playlistmaker.sharing.domain.ShareLinksOpenerInteractor
import com.practicum.playlistmaker.sharing.domain.interactor.ShareLinksOpenerInteractorImpl
import org.koin.dsl.module

val interactorModule = module{

    factory <MediaplayerInteractor>{ (callback: MyCallback) ->
        MediaplayerInteractorImpl(mediaplayer = MediaplayerRepositoryImpl(callback))
    }

    factory <MediaInteractor>{
        MediaInteractorImpl(mediaRepository = get())
    }

    factory <SearchHistoryFunctionsInteractor>{
        SearchHistoryFunctionsInteractorImpl(searchHistoryFunctions = get())
    }

    factory <ShareLinksOpenerInteractor>{
        ShareLinksOpenerInteractorImpl(shareLinksOpener = get())
    }

    factory <ThemeSwitcherInteractor>{
        ThemeSwitcherInteractorImpl(themeSwitcher = get())
    }

    factory <RetrofitSearcherInteractor> {
        RetrofitSearcherInteractorImpl(retrofitSearcher = get())
    }

    factory <PlaylistsInteractor> {
        PlaylistsInteractorImpl(playlistRepository = get())
    }
}
