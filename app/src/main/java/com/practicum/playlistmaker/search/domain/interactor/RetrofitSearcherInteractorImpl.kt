package com.practicum.playlistmaker.search.domain.interactor

import com.practicum.playlistmaker.search.domain.RetrofitSearcherInteractor
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository

class RetrofitSearcherInteractorImpl(private val retrofitSearcher: RetrofitSearcherRepository) : RetrofitSearcherInteractor {

    override fun setSearchText(searchText: String) {
        retrofitSearcher.setSearchText(searchText)
    }

    override fun searchDebounce() {
        retrofitSearcher.searchDebounce()
    }

    override fun goForApiSearch() {
        retrofitSearcher.goForApiSearch()
    }
}