package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.favourites.data.db.Database
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.data.network.ItunesSearchApi
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import com.practicum.playlistmaker.search.domain.state.SearchState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RetrofitSearcherRepositoryImpl(val itunesSearchService: ItunesSearchApi, val appDatabase: Database) : RetrofitSearcherRepository {

    //Работа c Itunes Search Api
    override fun goForApiSearch(): Flow<SearchState> = flow {
        if (searchText.isNotEmpty()) {
            emit(SearchState.Loading)
            try {
                val response = itunesSearchService.search(searchText)
                if (!response.results.isNullOrEmpty()) {
                    emit(SearchState.SearchContent((response).results.map {
                        Track(
                            it.trackId,
                            it.trackName,
                            it.artistName,
                            it.trackTime,
                            it.artworkUrl100,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl,
                            checkIsFavourite(it.trackId)
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

    private suspend fun checkIsFavourite(trackId: String): Boolean{
        val currentFavouriteTrackList = appDatabase.trackDao().getTrackIds()
        return (trackId in currentFavouriteTrackList)

    }
    override fun setSearchText(newSearchText: String){
        searchText = newSearchText
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}