package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.presentation.Mediaplayer
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity() : Mediaplayer() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        preparePlayer()
        initializeActivityWithTrackInfo()

        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()
        }

        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener{
            playbackControl()
        }



    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        release()
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

}