package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.search.domain.RetrofitSearcherInteractor
import com.practicum.playlistmaker.search.domain.SearchHistoryFunctionsInteractor
import com.practicum.playlistmaker.search.domain.model.RetrofitCallback
import com.practicum.playlistmaker.search.domain.state.SearchState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf


class SearchViewModel : ViewModel(), MyCallback, RetrofitCallback, KoinComponent {

    private val searchHistoryLiveData = MutableLiveData<MutableList<Track>>()
    private val searchLiveData = MutableLiveData<MutableList<Track>>()
    private val state = MutableLiveData<SearchState>()
    private val searchHistoryRepositoryInteractor: SearchHistoryFunctionsInteractor by inject(){
        parametersOf(this)
    }
    private val retrofitSearcherInteractor: RetrofitSearcherInteractor by inject() {
        parametersOf(this,this)
    }
    fun getState(): LiveData<SearchState> = state
    fun getSearchHistoryLiveData(): LiveData<MutableList<Track>> = searchHistoryLiveData
    private fun getCurrentSearchHistory(): MutableList<Track> {
        return searchHistoryLiveData.value ?: mutableListOf()
    }

    fun getSearchLiveData(): LiveData<MutableList<Track>> = searchLiveData
    private fun getCurrentSearchLiveData(): MutableList<Track> {
        return searchLiveData.value ?: mutableListOf()
    }

    init{
        read()
    }

    fun addTrackToHistory(track: Track){
        val trackHistoryList = searchHistoryLiveData.value ?: mutableListOf()
        if (track in trackHistoryList){
            trackHistoryList.remove(track)
            trackHistoryList.add(track)
        }
        else{
            if (trackHistoryList.size < 10) {
                trackHistoryList.add(track)
            }
            else{
                trackHistoryList.removeAt(0)
                trackHistoryList.add(track)
            }
        }
        searchHistoryLiveData.value = trackHistoryList
        write()
    }

    private fun read(){
        searchHistoryLiveData.value = searchHistoryRepositoryInteractor.read()
    }

    fun stateHistoryContent(){
        state.value = SearchState.HistoryContent(getCurrentSearchHistory())
    }

    fun stateSearchContent(){
        state.value = SearchState.SearchContent(getCurrentSearchLiveData())
    }

    fun clearSearchButton(){
        searchLiveData.value = mutableListOf()
        state.value = SearchState.HistoryContent(getCurrentSearchHistory())
    }


    fun clearHistory(){
        searchHistoryLiveData.value = mutableListOf()
        searchHistoryRepositoryInteractor.clear()
        state.value = SearchState.HistoryContent(getCurrentSearchHistory())
    }

    private fun write(){
        searchHistoryRepositoryInteractor.write(getCurrentSearchHistory())
    }

    fun searchDebounce(searchText: String){
        retrofitSearcherInteractor.setSearchText(searchText)
        retrofitSearcherInteractor.searchDebounce()
    }

    fun goForApiSearch(searchText: String){
        retrofitSearcherInteractor.setSearchText(searchText)
        retrofitSearcherInteractor.goForApiSearch()
    }

    override fun execute(message: String) {
        when (message){
            "StateContent" -> state.value = SearchState.SearchContent(getCurrentSearchLiveData())
            "StateNothingFound" -> state.value = SearchState.NothingFound
            "StateNoInternet" -> state.value = SearchState.NoInternet
            "StateLoading" -> state.value = SearchState.Loading
        }
    }

    override fun execute(trackList: MutableList<Track>) {
        searchLiveData.value = trackList
    }

}