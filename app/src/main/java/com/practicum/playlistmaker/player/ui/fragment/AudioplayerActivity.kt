package com.practicum.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.view_model.AudioplayerViewModel
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


class AudioplayerActivity : AppCompatActivity(), KoinComponent {

    private lateinit var binding: ActivityAudioplayerBinding
    private lateinit var previewUrl: String
    private lateinit var currentTrack: Track
    private val viewModel: AudioplayerViewModel by inject{
        parametersOf(currentTrack)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra(TRACK_JSON)
        currentTrack= Json.decodeFromString<Track>(trackJson!!)
        previewUrl  = Json.decodeFromString<Track>(trackJson!!).previewUrl
        initializeActivityWithTrackInfo()
        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
            changeTimer(playStatus)
        }
        viewModel.getTrackIsFavouriteLiveData().observe(this){ isFavourite->
            changeLikeColor(isFavourite)
        }
        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()
        }
        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener{
            viewModel.playButtonClick()
        }

        binding.audioplayerRightButton.setOnClickListener {
            if (currentTrack.isFavourite){
                binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button)
                viewModel.deleteTrack()
                currentTrack.isFavourite = false
            }
            else {
                binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button_active)
                viewModel.insertTrack()
                currentTrack.isFavourite = true
            }
        }

    }

    private fun changeButtonStyle(playStatus: PlayStatus){
        when (playStatus.isPlaying){
            true -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_pause)
            false -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(binding.audioplayerCenterButton.context, R.drawable.audioplayer_center_button_play)
        }
    }

    private fun changeLikeColor(isFavourite: Boolean){
        if (isFavourite)
            binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button_active)
        else
            binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button)
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
        val trackJson = intent.getStringExtra(TRACK_JSON)
        val currentTrack: Track = Json.decodeFromString<Track>(trackJson!!)
        val trackIsFavourite = viewModel.checkFavourite()
        binding.trackName.text = currentTrack.trackName
        binding.trackAuthor.text = currentTrack.artistName
        binding.trackDurationText.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack.trackTime)
        binding.trackAlbumText.text = currentTrack.collectionName
        binding.trackYearText.text = currentTrack.releaseDate?.take(4)
        binding.trackGenreText.text = currentTrack.primaryGenreName
        binding.trackCountryText.text = currentTrack.country
        binding.trackAlbum.visibility = View.VISIBLE
        binding.trackAlbumText.visibility = View.VISIBLE
        if (currentTrack.isFavourite)
            binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button_active)
        else
            binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button)
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

        private const val TRACK_JSON = "trackJson"

        // Пробрасываем аргументы в Bundle
        fun createArgs(trackJson: String): Bundle =
            bundleOf(TRACK_JSON to trackJson)

    }
}