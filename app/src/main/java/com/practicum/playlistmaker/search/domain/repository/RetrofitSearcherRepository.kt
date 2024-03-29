package com.practicum.playlistmaker.search.domain.repository

interface RetrofitSearcherRepository {
    fun goForApiSearch()
    fun searchDebounce()
    fun setSearchText(newSearchText: String)
}