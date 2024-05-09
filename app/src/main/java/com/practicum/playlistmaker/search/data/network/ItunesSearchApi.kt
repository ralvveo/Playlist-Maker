package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.TracksResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): TracksResponse
}

