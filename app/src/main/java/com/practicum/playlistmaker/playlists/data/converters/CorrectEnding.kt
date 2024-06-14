package com.practicum.playlistmaker.playlists.data.converters

object CorrectEnding {
     fun doEnding(tracksCount: Int): String{
        return if (tracksCount in 11..14) "треков"
        else{
            when (tracksCount % 10){
                1 -> "трек"
                2,3,4 -> "трека"
                else -> "треков"
            }
        }
    }
}