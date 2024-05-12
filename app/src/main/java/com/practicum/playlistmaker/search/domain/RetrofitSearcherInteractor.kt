package com.practicum.playlistmaker.search.domain

import com.practicum.playlistmaker.search.domain.state.SearchState
import kotlinx.coroutines.flow.Flow

interface RetrofitSearcherInteractor {
    fun setSearchText(searchText: String)
    fun goForApiSearch(): Flow<SearchState>
}