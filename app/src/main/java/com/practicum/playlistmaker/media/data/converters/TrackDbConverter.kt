package com.practicum.playlistmaker.media.data.converters

import com.practicum.playlistmaker.media.data.db.entity.TrackEntity
import com.practicum.playlistmaker.player.domain.model.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity{
        return TrackEntity(track.trackId, track.trackName, track.artistName, track.trackTime, track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, track.isFavourite)
    }

    fun map(track: TrackEntity): Track{
        return Track(track.trackId, track.trackName, track.artistName, track.trackTime, track.artworkUrl100, track.collectionName, track.releaseDate, track.primaryGenreName, track.country, track.previewUrl, track.isFavourite)
    }

}

