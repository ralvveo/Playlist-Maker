package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.RetrofitSearcherInteractor
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import com.practicum.playlistmaker.search.domain.state.SearchState
import kotlinx.coroutines.flow.Flow

class RetrofitSearcherInteractorImpl(private val retrofitSearcher: RetrofitSearcherRepository) : RetrofitSearcherInteractor {

    override fun setSearchText(searchText: String) {
        retrofitSearcher.setSearchText(searchText)
    }

    override fun goForApiSearch(): Flow<SearchState> {
        return retrofitSearcher.goForApiSearch()
    }
}