package com.practicum.playlistmaker.search.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.repository.SearchHistoryFunctionsRepository

class SearchHistoryFunctionsRepositoryImpl(val context: Context) : SearchHistoryFunctionsRepository {

    private val sharedPrefs = context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, AppCompatActivity.MODE_PRIVATE)

    //Чтение Истории Поиска из Shared Preferences
    override fun read(): MutableList<Track> {
        val json = sharedPrefs!!.getString(SEARCH_HISTORY, null) ?: return mutableListOf<Track>()
        val mutableListTrackType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, mutableListTrackType)
    }

    //Сохранение Истории Поиска в Shared Preferences
    override fun write(trackList: MutableList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPrefs!!.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    //Очистка Истории Поиска в Shared Preferences
    override fun clear(){
        sharedPrefs!!.edit()
            .remove(SEARCH_HISTORY)
            .apply()
    }

    companion object{
        const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
        const val SEARCH_HISTORY = "search_history"
    }
}