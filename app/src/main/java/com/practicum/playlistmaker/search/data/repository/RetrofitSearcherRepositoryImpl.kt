package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.network.ItunesSearchApi
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import com.practicum.playlistmaker.search.domain.state.SearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrofitSearcherRepositoryImpl(val itunesSearchService: ItunesSearchApi) : RetrofitSearcherRepository {

    //Работа c Itunes Search Api
    override fun goForApiSearch(): Flow<SearchState> = flow {
        if (searchText.isNotEmpty()) {
            emit(SearchState.Loading)
            try {
                val response = itunesSearchService.search(searchText)
                if (!response.results.isNullOrEmpty()) {
                    emit(SearchState.SearchContent((response).results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            it.trackTime,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }))
                } else {
                    emit(SearchState.NothingFound)
                }
            }
            catch (e:Throwable){
                emit(SearchState.NoInternet)
            }
        }
        else{
        }
    }
    private var searchText = ""

    override fun setSearchText(newSearchText: String){
        searchText = newSearchText
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}