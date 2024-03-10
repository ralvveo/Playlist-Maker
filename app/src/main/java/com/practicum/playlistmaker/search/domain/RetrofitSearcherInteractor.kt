package com.practicum.playlistmaker.search.domain

interface RetrofitSearcherInteractor {
    fun setSearchText(searchText: String)
    fun searchDebounce()
    fun goForApiSearch()
}