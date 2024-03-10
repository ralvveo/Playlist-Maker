package com.practicum.playlistmaker.search.domain.state

import com.practicum.playlistmaker.player.domain.model.Track


sealed interface SearchState{
    object Loading : SearchState
    object NothingFound: SearchState
    object NoInternet: SearchState
    data class SearchContent(val searchList: List<Track>) : SearchState
    data class HistoryContent(val historyList: List<Track>) : SearchState
}