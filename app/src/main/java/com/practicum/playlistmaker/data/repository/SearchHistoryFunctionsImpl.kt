package com.practicum.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.SEARCH_HISTORY
import com.practicum.playlistmaker.ui.SearchActivity.Companion.trackHistoryList
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.SearchHistoryFunctions
import com.practicum.playlistmaker.ui.TracksAdapter

class SearchHistoryFunctionsImpl(val sharedPrefs: SharedPreferences?) : SearchHistoryFunctions{


    //Добавление трека в Историю Поиска(без сохранения в Shared Preferences)
    override fun addTrackToHistory(track: Track){
        if (track in trackHistoryList){
            trackHistoryList.remove(track)
            trackHistoryList.add(track)
        }
        else{
            if (trackHistoryList.size < 10)
                trackHistoryList.add(track)
            else{
                trackHistoryList.removeAt(0)
                trackHistoryList.add(track)
            }
        }
        val trackHistoryAdapter = TracksAdapter(trackHistoryList)
        trackHistoryAdapter.notifyDataSetChanged()
    }

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
}