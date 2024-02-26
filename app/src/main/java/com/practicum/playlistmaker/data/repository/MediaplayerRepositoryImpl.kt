package com.practicum.playlistmaker.data.repository

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.model.PlayerState
import com.practicum.playlistmaker.domain.repository.MediaplayerRepository

import com.practicum.playlistmaker.presentation.PlayerStateListenerImpl
import java.text.SimpleDateFormat
import java.util.Locale

open class MediaplayerRepositoryImpl (val binding: ActivityAudioplayerBinding): AppCompatActivity(), MediaplayerRepository{
    private var mediaPlayer: MediaPlayer = MediaPlayer()
    private var handler = Handler(Looper.getMainLooper())
    private var playerState: PlayerState = PlayerState.STATE_DEFAULT

    //Constants
    companion object{
        const val TIMER_UPDATE_TIME = 500L
        const val SONGS_DURATION_TIME = 30000
        const val DEFAULT_TIME = "00:00"
    }

    override fun preparePlayer(previewUrl: String, listener: PlayerStateListenerImpl) {
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.audioplayerCenterButton.isEnabled = true
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            listener.changeStateExecute(playerState)
            binding.trackCurrentTime.text = DEFAULT_TIME
            //binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_play)
            handler.removeCallbacksAndMessages(createUpdateTimerTask())
        }

    }

    override fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        playerState = PlayerState.STATE_PLAYING

    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(createUpdateTimerTask())
        playerState = PlayerState.STATE_PAUSED

    }

    override fun playbackControl() {
        when(playerState) {
            PlayerState.STATE_PLAYING -> {
                pausePlayer()
            }
            PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
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
                if (playerState == PlayerState.STATE_PLAYING){
                    val elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    if (mediaPlayer.currentPosition < SONGS_DURATION_TIME){
                        binding.trackCurrentTime.text = elapsedTime
                        handler.postDelayed(this, TIMER_UPDATE_TIME)
                    }
                    else{
                        binding.trackCurrentTime.text = DEFAULT_TIME

                    }
                }
            }
        }
    }

    override fun getState(): PlayerState{
        return playerState
    }

    override fun setState(changedPlayerState: PlayerState){
        playerState = changedPlayerState
    }
}
