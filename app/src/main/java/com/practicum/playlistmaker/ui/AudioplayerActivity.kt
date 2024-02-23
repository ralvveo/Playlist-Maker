package com.practicum.playlistmaker.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.model.Track
import com.practicum.playlistmaker.domain.repository.MediaplayerActivity
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var getMediaplayerActivity: MediaplayerActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getMediaplayerActivity = Creator.provideMediaplayer(binding)
        val trackJson = intent.getStringExtra("trackJson")
        val previewUrl  = Json.decodeFromString<Track>(trackJson!!).previewUrl
        getMediaplayerActivity.preparePlayer(previewUrl = previewUrl)
        initializeActivityWithTrackInfo()

        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()
        }

        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener{
            getMediaplayerActivity.playbackControl()
        }



    }

    override fun onPause() {
        super.onPause()
        getMediaplayerActivity.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        getMediaplayerActivity.release()
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