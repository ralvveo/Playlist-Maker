package com.practicum.playlistmaker.search.data.repository

import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import com.practicum.playlistmaker.search.data.dto.TracksResponse
import com.practicum.playlistmaker.search.data.network.ItunesSearchApi
import com.practicum.playlistmaker.search.domain.model.RetrofitCallback
import com.practicum.playlistmaker.search.domain.repository.RetrofitSearcherRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitSearcherRepositoryImpl(val callback: MyCallback, val retrofitCallback: RetrofitCallback) : RetrofitSearcherRepository {

    //Работа c Itunes Search Api
    private val itunesSearchBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchService = retrofit.create(ItunesSearchApi::class.java)

    //Работа c Itunes Search Api
    override fun goForApiSearch(){
        val trackList: MutableList<Track> = mutableListOf()
        if (searchText.isNotEmpty()) {
            callback.execute("StateLoading")
            itunesSearchService.search(searchText).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            callback.execute("StateContent")
                            retrofitCallback.execute(trackList)
                        }
                        if (trackList.isEmpty()) {
                            trackList.clear()
                            callback.execute("StateNothingFound")
                        }
                    }
                    else {
                        callback.execute("StateNoInternet")
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    callback.execute("StateNoInternet",)
                }
            })
        }
        else{
        }
    }
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {goForApiSearch()}
    private var searchText = ""

    override fun setSearchText(newSearchText: String){
        searchText = newSearchText
    }

    override fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    companion object {
        const val SEARCH_DEBOUNCE_DELAY = 2000L

    }
}