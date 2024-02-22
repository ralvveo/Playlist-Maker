package com.practicum.playlistmaker.presentation

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

abstract class Mediaplayer : AppCompatActivity(){
    var mediaPlayer: MediaPlayer = MediaPlayer()
    lateinit var binding: ActivityAudioplayerBinding
    private var handler = Handler(Looper.getMainLooper())
    val DELAY = 500L
    var playerState: PlayerState = PlayerState.STATE_DEFAULT



    fun preparePlayer() {
        val previewUrl = intent.getStringExtra("previewUrl")
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.audioplayerCenterButton.isEnabled = true
            playerState = PlayerState.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = PlayerState.STATE_PREPARED
            binding.trackCurrentTime.text = "00:00"
            binding.audioplayerCenterButton.background = getDrawable(R.drawable.audioplayer_center_button_play)
            handler.removeCallbacksAndMessages(createUpdateTimerTask())
        }
    }

    fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        playerState = PlayerState.STATE_PLAYING
        binding.audioplayerCenterButton.background = getDrawable(R.drawable.audioplayer_center_button_pause)
    }

    fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(createUpdateTimerTask())
        playerState = PlayerState.STATE_PAUSED
        binding.audioplayerCenterButton.background = getDrawable(R.drawable.audioplayer_center_button_play)
    }

    fun startTimer(){
        handler.post(createUpdateTimerTask())
    }


    fun createUpdateTimerTask() : Runnable{
        return object: Runnable{
            override fun run() {
                if (playerState == PlayerState.STATE_PLAYING){
                    val elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    if (mediaPlayer.currentPosition < 30000){
                        binding.trackCurrentTime.text = elapsedTime
                        handler.postDelayed(this, DELAY)
                    }
                    else{
                        binding.trackCurrentTime.text = "00:00"
                    }
                }
            }
        }
    }

    fun playbackControl() {
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

    fun release(){
        mediaPlayer.release()
    }



}
