package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.player.domain.repository.MyCallback
import java.text.SimpleDateFormat
import java.util.Locale

open class MediaplayerRepositoryImpl (val callback: MyCallback): MediaplayerRepository {
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())
    private var playStatus: PlayStatus = PlayStatus(progress = DEFAULT_TIME, isPlaying = false)

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
            handler.removeCallbacksAndMessages(createUpdateTimerTask())
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        playStatus.isPlaying = true
        callback.execute("Play")

    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(createUpdateTimerTask())
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

    private fun startTimer(){
        handler.post(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask() : Runnable{
        return object: Runnable{
            override fun run() {
                if (playStatus.isPlaying){
                    val elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    if (mediaPlayer.currentPosition < SONGS_DURATION_TIME){
                        callback.execute(elapsedTime)
                        handler.postDelayed(this, TIMER_UPDATE_TIME)
                    }
                    else{
                        callback.execute("TrackFinished")

                    }
                }
            }
        }
    }

    override fun getStatus(): PlayStatus {
        return playStatus
    }

    override fun setStatus(changedPlayStatus: PlayStatus){
        playStatus = changedPlayStatus
    }

    //Constants
    companion object{
        const val DEFAULT_TIME = "00:00"
        const val TIMER_UPDATE_TIME = 500L
        const val SONGS_DURATION_TIME = 30000
    }
}
