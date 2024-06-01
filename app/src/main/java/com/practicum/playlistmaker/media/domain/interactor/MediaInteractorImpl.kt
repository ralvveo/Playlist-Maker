package com.practicum.playlistmaker.media.domain.interactor

import com.practicum.playlistmaker.media.domain.db.MediaInteractor
import com.practicum.playlistmaker.media.domain.repository.MediaRepository
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class MediaInteractorImpl(private val mediaRepository: MediaRepository) : MediaInteractor {


    override suspend fun insertTrack(track: Track) {
        mediaRepository.insertTrack(track)
    }

    override suspend fun deleteTrack(track: Track) {
        mediaRepository.deleteTrack(track)
    }

    override fun getTrackList(): Flow<List<Track>> {
        return mediaRepository.getTrackList()
    }

    override fun getTrackIds(): Flow<List<String>> {
        return mediaRepository.getTrackIds()
    }
}