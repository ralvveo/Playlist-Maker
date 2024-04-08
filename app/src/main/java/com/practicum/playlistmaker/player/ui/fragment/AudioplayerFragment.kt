package com.practicum.playlistmaker.player.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentAudioplayerBinding
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.view_model.AudioplayerViewModel
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


class AudioplayerFragment : Fragment(), KoinComponent {

    private lateinit var binding: FragmentAudioplayerBinding
    private lateinit var previewUrl: String
    private val viewModel: AudioplayerViewModel by inject{
        parametersOf(previewUrl)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentAudioplayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackJson = requireArguments().getString(TRACK_JSON)
        previewUrl  = Json.decodeFromString<Track>(trackJson!!).previewUrl
        initializeActivityWithTrackInfo()
        viewModel.getPlayStatusLiveData().observe(viewLifecycleOwner) { playStatus ->
            changeButtonStyle(playStatus)
            changeTimer(playStatus)
        }
        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            findNavController().navigateUp()
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
        val trackJson = requireArguments().getString(TRACK_JSON)
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

        private const val TRACK_JSON = "track_json"

        // Пробрасываем аргументы в Bundle
        fun createArgs(trackJson: String): Bundle =
            bundleOf(TRACK_JSON to trackJson)

    }
}