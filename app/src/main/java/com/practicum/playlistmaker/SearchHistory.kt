package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.App.Companion.trackHistoryList

class SearchHistory() {
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
        val trackHistoryAdapter = TracksAdapter(trackHistoryList)
        trackHistoryAdapter.notifyDataSetChanged()
    }

    fun read(sharedPreferences: SharedPreferences): MutableList<Track> {
        val json = sharedPreferences.getString(SEARCH_HISTORY, null) ?: return mutableListOf<Track>()
        val mutableListTrackType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, mutableListTrackType)
    }

    // запись
    fun write(sharedPreferences: SharedPreferences, trackList: MutableList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    fun clear(sharedPreferences: SharedPreferences){
        sharedPreferences.edit()
            .remove(SEARCH_HISTORY)
            .apply()


    }
}