package com.practicum.playlistmaker.media.data.repository

import com.practicum.playlistmaker.media.data.converters.TrackDbConverter
import com.practicum.playlistmaker.media.data.db.AppDatabase
import com.practicum.playlistmaker.media.domain.repository.MediaRepository
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList

class MediaRepositoryImpl(private val database: AppDatabase, private val converter: TrackDbConverter) : MediaRepository {
    override suspend fun insertTrack(track: Track) {
        database.trackDao().insertTrack(converter.map(track))
    }

    override suspend fun deleteTrack(track: Track) {
        database.trackDao().deleteTrack(converter.map(track))
    }

    override fun getTrackList(): Flow<List<Track>> = flow{
        val trackList = database.trackDao().getTracks()
        emit(trackList.map{ trackEntity -> converter.map(trackEntity) })
    }

    override fun getTrackIds(): Flow<List<String>> = flow{
        val trackIds = database.trackDao().getTrackIds()
        emit(trackIds)
    }



}