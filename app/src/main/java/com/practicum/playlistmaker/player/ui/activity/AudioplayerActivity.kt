package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.view_model.AudioplayerViewModel
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var viewModel: AudioplayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val trackJson = intent.getStringExtra("trackJson")
        val previewUrl  = Json.decodeFromString<Track>(trackJson!!).previewUrl
        viewModel = ViewModelProvider(this, AudioplayerViewModel.factory(trackId = previewUrl))[AudioplayerViewModel::class.java]
        initializeActivityWithTrackInfo()
        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
            changeTimer(playStatus)
        }
        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()
        }
        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener{
            viewModel.playButtonClick()
        }
    }

    private fun changeButtonStyle(playStatus: PlayStatus){
        when (playStatus.isPlaying){
            true -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_pause)
            false -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_play)
        }
    }

    private fun changeTimer(playStatus: PlayStatus){
        binding.trackCurrentTime.text = playStatus.progress
    }
    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
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