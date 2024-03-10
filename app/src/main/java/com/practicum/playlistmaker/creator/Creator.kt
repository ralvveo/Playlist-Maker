package com.practicum.playlistmaker.creator

import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.player.data.MediaplayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.MediaplayerInteractor
import com.practicum.playlistmaker.player.domain.interactor.MediaplayerInteractorImpl
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.search.data.repository.RetrofitSearcherRepositoryImpl
import com.practicum.playlistmaker.search.data.repository.SearchHistoryFunctionsRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchHistoryFunctionsInteractor
import com.practicum.playlistmaker.search.domain.interactor.RetrofitSearcherInteractorImpl
import com.practicum.playlistmaker.search.domain.interactor.SearchHistoryFunctionsInteractorImpl
import com.practicum.playlistmaker.search.domain.model.RetrofitCallback
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryFunctionsRepository
import com.practicum.playlistmaker.settings.data.ThemeSwitcherRepositoryImpl
import com.practicum.playlistmaker.settings.domain.ThemeSwitcherInteractor
import com.practicum.playlistmaker.settings.domain.interactor.ThemeSwitcherInteractorImpl
import com.practicum.playlistmaker.settings.domain.repository.ThemeSwitcherRepository
import com.practicum.playlistmaker.sharing.data.ShareLinksOpenerRepositoryImpl
import com.practicum.playlistmaker.sharing.domain.ShareLinksOpenerInteractor
import com.practicum.playlistmaker.sharing.domain.interactor.ShareLinksOpenerInteractorImpl
import com.practicum.playlistmaker.sharing.domain.repository.ShareLinksOpenerRepository

object Creator {

    private lateinit var applicationContext: App
    fun init (newApplicationContext: App){
        applicationContext = newApplicationContext
    }

    fun provideMediaplayerInteractor(callback: MyCallback) : MediaplayerInteractor {
        return MediaplayerInteractorImpl(mediaplayer = provideMediaplayer(callback))
    }

    fun provideSearchHistoryFunctionsInteractor(): SearchHistoryFunctionsInteractor {
        return SearchHistoryFunctionsInteractorImpl(searchHistoryFunctions = provideSearchHistoryFunctions())
    }

    fun provideShareLinksOpenerInteractor(): ShareLinksOpenerInteractor {
        return ShareLinksOpenerInteractorImpl(shareLinksOpener = provideShareLinksOpener())
    }

    fun provideThemeSwitcherInteractor() : ThemeSwitcherInteractor {
        return ThemeSwitcherInteractorImpl(themeSwitcher = provideThemeSwitcher())
    }

    fun provideRetrofitSearcherInteractor(callback: MyCallback, retrofitCallback: RetrofitCallback): RetrofitSearcherInteractorImpl {
        return RetrofitSearcherInteractorImpl(retrofitSearcher = provideRetrofitSearcher(callback = callback, retrofitCallback = retrofitCallback))
    }


    private fun provideMediaplayer(callback: MyCallback): MediaplayerRepositoryImpl {
        return MediaplayerRepositoryImpl(callback)
    }

    private fun provideSearchHistoryFunctions(): SearchHistoryFunctionsRepository {
        return SearchHistoryFunctionsRepositoryImpl(applicationContext)
    }

    private fun provideShareLinksOpener(): ShareLinksOpenerRepository {
        return ShareLinksOpenerRepositoryImpl(applicationContext)
    }

    private fun provideThemeSwitcher(): ThemeSwitcherRepository {
        return ThemeSwitcherRepositoryImpl(applicationContext)
    }

    private fun provideRetrofitSearcher(callback: MyCallback, retrofitCallback: RetrofitCallback): RetrofitSearcherRepository{
        return RetrofitSearcherRepositoryImpl(callback, retrofitCallback)
    }

}