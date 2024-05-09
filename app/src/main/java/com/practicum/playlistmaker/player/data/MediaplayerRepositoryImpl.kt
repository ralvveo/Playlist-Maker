package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.Closeable
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MediaplayerRepositoryImpl (val callback: MyCallback): MediaplayerRepository {

    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var playStatus: PlayStatus = PlayStatus(progress = DEFAULT_TIME, isPlaying = false)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun preparePlayer(previewUrl: String) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playStatus = PlayStatus(progress = DEFAULT_TIME, isPlaying = false)
        }
        mediaPlayer.setOnCompletionListener {
            playStatus = PlayStatus(progress = DEFAULT_TIME, isPlaying = false)
            callback.execute("Play")
            callback.execute("TrackFinished")
        }

    }


    override fun startPlayer() {
        mediaPlayer.start()
        playStatus.isPlaying = true
        callback.execute("Play")
        startTimer()

    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playStatus.isPlaying = false
        callback.execute("Pause")

    }

    override fun playbackControl() {
        when(playStatus.isPlaying) {
            true -> {
                pausePlayer()
            }
            false -> {
                startPlayer()
            }
        }
    }

    override fun release(){
        mediaPlayer.release()
    }

    private fun startTimer() {
        scope.launch {
            updateTimer()
        }
    }

    suspend fun updateTimer(){
        if (playStatus.isPlaying){
            val elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
            if (mediaPlayer.currentPosition < SONGS_DURATION_TIME){
                callback.execute(elapsedTime)
                delay(TIMER_UPDATE_TIME)
                updateTimer()
            }
            else{
                callback.execute("TrackFinished")
            }
        }
    }

    //Constants
    companion object{
        const val DEFAULT_TIME = "00:00"
        const val TIMER_UPDATE_TIME = 300L
        const val SONGS_DURATION_TIME = 30000
    }
}
