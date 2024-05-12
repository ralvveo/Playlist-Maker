package com.practicum.playlistmaker.search.domain.repository

import com.practicum.playlistmaker.search.domain.state.SearchState
import kotlinx.coroutines.flow.Flow

interface RetrofitSearcherRepository {
    fun goForApiSearch(): Flow<SearchState>
    fun setSearchText(newSearchText: String)
}