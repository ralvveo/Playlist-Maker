package com.practicum.playlistmaker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.App.Companion.trackHistoryList

class SearchHistory(val sharedPreferences: SharedPreferences?) {
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

    fun read(): MutableList<Track> {
        val json = sharedPreferences!!.getString(SEARCH_HISTORY, null) ?: return mutableListOf<Track>()
        val mutableListTrackType = object : TypeToken<MutableList<Track>>() {}.type
        return Gson().fromJson(json, mutableListTrackType)
    }

    // запись
    fun write(trackList: MutableList<Track>) {
        val json = Gson().toJson(trackList)
        sharedPreferences!!.edit()
            .putString(SEARCH_HISTORY, json)
            .apply()
    }

    fun clear(){

        sharedPreferences!!.edit()
            .remove(SEARCH_HISTORY)
            .apply()


    }
}