package com.practicum.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ItunesSearchApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse>
}

