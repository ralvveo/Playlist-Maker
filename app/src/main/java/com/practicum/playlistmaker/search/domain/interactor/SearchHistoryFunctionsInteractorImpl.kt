package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.SearchHistoryFunctionsInteractor
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryFunctionsRepository

class SearchHistoryFunctionsInteractorImpl(private val searchHistoryFunctions: SearchHistoryFunctionsRepository) : SearchHistoryFunctionsInteractor{
    override fun read(): MutableList<Track>{
        return searchHistoryFunctions.read()
    }

    override fun write(trackList: MutableList<Track>) {
        searchHistoryFunctions.write(trackList)
    }

    override fun clear() {
        searchHistoryFunctions.clear()
    }


}