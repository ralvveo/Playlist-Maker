package com.practicum.playlistmaker

import android.content.Intent
import android.content.SharedPreferences
import androidx.core.content.ContextCompat.startActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.SearchActivity.Companion.trackHistoryList

class SearchHistory() {
    //Добавление трека в Историю Поиска(без сохранения в Shared Preferences)
    fun addTrackToHistory(track: Track){
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
        //val trackHistoryAdapter = TracksAdapter(trackHistoryList)
        //trackHistoryAdapter.notifyDataSetChanged()
    }

    //Чтение Истории Поиска из Shared Preferences
    fun read(sharedPreferences: SharedPreferences): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return mutableListOf<Track>()
        val mutableListTrackType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, mutableListTrackType)
    }

    //Сохранение Истории Поиска в Shared Preferences
    fun write(sharedPreferences: SharedPreferences, trackList: MutableList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    //Очистка Истории Поиска в Shared Preferences
    fun clear(sharedPreferences: SharedPreferences){
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY)
            .apply()


    }
}