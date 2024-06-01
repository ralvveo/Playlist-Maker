package com.practicum.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.media.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: TrackEntity)

    @Query("SELECT * FROM tracks_table")
    suspend fun getTracks(): List<TrackEntity>

    @Query( "SELECT trackId FROM tracks_table")
    suspend fun getTrackIds(): List<String>

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrack(track: TrackEntity)

}
