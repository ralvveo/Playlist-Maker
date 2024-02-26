package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.model.PlayerState
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.MediaplayerRepository
import com.practicum.playlistmaker.presentation.PlayerStateListenerImpl
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var getMediaplayerRepository: MediaplayerRepository
    private lateinit var getPlayerStateListener: PlayerStateListenerImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMediaplayerRepository = Creator.provideMediaplayer(binding)
        getPlayerStateListener = PlayerStateListenerImpl()
        val trackJson = intent.getStringExtra("trackJson")
        val previewUrl  = Json.decodeFromString<Track>(trackJson!!).previewUrl
        getMediaplayerRepository.preparePlayer(previewUrl = previewUrl, listener = getPlayerStateListener)
        initializeActivityWithTrackInfo()

        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()
        }

        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener{
            when (getMediaplayerRepository.getState()){
                PlayerState.STATE_PLAYING -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_play)
                PlayerState.STATE_PREPARED, PlayerState.STATE_PAUSED -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_pause)
                else -> {}
            }
            getMediaplayerRepository.playbackControl()
        }



    }

    override fun onPause() {
        super.onPause()
        getMediaplayerRepository.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        getMediaplayerRepository.release()
    }


    private fun initializeActivityWithTrackInfo(){
        val trackJson = intent.getStringExtra("trackJson")
        val currentTrack: Track = Json.decodeFromString<Track>(trackJson!!)
        binding.trackName.text = currentTrack.trackName
        binding.trackAuthor.text = currentTrack.artistName
        binding.trackDurationText.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack.trackTime)
        binding.trackAlbumText.text = currentTrack.collectionName
        binding.trackYearText.text = currentTrack.releaseDate?.take(4)
        binding.trackGenreText.text = currentTrack.primaryGenreName
        binding.trackCountryText.text = currentTrack.country
        binding.trackAlbum.visibility = View.VISIBLE
        binding.trackAlbumText.visibility = View.VISIBLE
        val infoTrackImage512 = currentTrack.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
        val roundedCornersSize = 8
        Glide.with(binding.trackImage)
            .load(infoTrackImage512)
            .transform(RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(binding.trackImage)

        if (binding.trackAlbumText.text.isNullOrEmpty()){
            binding.trackAlbumText.visibility = View.GONE
            binding.trackAlbum.visibility = View.GONE
        }

    }



    companion object {
        fun changeCenterButton(changePicture: String) {
            when (changePicture) {
                "play" -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(
                    binding.audioplayerCenterButton.context,
                    R.drawable.audioplayer_center_button_play
                )

                "pause" -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(
                    binding.audioplayerCenterButton.context,
                    R.drawable.audioplayer_center_button_pause
                )
            }
        }

    }


}