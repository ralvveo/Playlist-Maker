package com.practicum.playlistmaker.player.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.player.domain.model.AddedTrackStatus
import com.practicum.playlistmaker.player.domain.model.PlayStatus
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.view_model.AudioplayerViewModel
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlist.ui.fragment.NewPlaylistFragment
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
    private val viewModel: AudioplayerViewModel by inject {
        parametersOf(currentTrack)
    }
    private lateinit var playlistListAdapter: PlaylistListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra(TRACK_JSON)?: ""
        currentTrack = Json.decodeFromString<Track>(trackJson)
        previewUrl = Json.decodeFromString<Track>(trackJson).previewUrl
        playlistListAdapter = PlaylistListAdapter(viewModel::addTrackToPlaylist)

        binding.recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.recyclerView.adapter = playlistListAdapter

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
        initializeActivityWithTrackInfo()

        val newPlaylistFragment: Fragment? = supportFragmentManager.findFragmentByTag(NEWPLAYLIST_TAG)
        if (newPlaylistFragment != null) {
            binding.overlay.visibility = View.GONE
            binding.audioplayerContent.visibility = View.GONE
            binding.bottomSheet.visibility = View.GONE
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus)
            changeTimer(playStatus)
        }
        viewModel.getTrackIsFavouriteLiveData().observe(this) { isFavourite ->
            changeLikeColor(isFavourite)
        }

        viewModel.getPlaylistListLiveData().observe(this) { playlistList ->
            renderPlaylistList(playlistList)

        }

        viewModel.getTrackAddedLiveData().observe(this){trackAdded ->
            renderAddedToast(trackAdded)

        }
        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()
        }
        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener {
            viewModel.playButtonClick()
        }

        //Кнопка новый плейлист
        binding.newPlaylistButton2.setOnClickListener {
            binding.overlay.visibility = View.GONE
            supportFragmentManager.beginTransaction()
                .replace(R.id.rootFragmentContainerView, NewPlaylistFragment(), NEWPLAYLIST_TAG)
                .commit()
            binding.audioplayerContent.visibility = View.GONE
            binding.bottomSheet.visibility = View.GONE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            viewModel.checkPlaylists()
        }

        binding.audioplayerRightButton.setOnClickListener {
            if (currentTrack.isFavourite) {
                binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button)
                currentTrack.isFavourite = false
                viewModel.deleteTrack()
            } else {
                binding.audioplayerRightButton.background = getDrawable(R.drawable.audioplayer_right_button_active)
                currentTrack.isFavourite = true
                viewModel.insertTrack()
            }
        }



        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN, BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.overlay.visibility = View.GONE
                    }

                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })

        binding.audioplayerLeftButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            viewModel.checkPlaylists()
        }

    }

    private fun changeButtonStyle(playStatus: PlayStatus) {
        when (playStatus.isPlaying) {
            true -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(
                binding.audioplayerCenterButton.context,
                R.drawable.audioplayer_center_button_pause
            )

            false -> binding.audioplayerCenterButton.background = ContextCompat.getDrawable(
                binding.audioplayerCenterButton.context,
                R.drawable.audioplayer_center_button_play
            )
        }
    }

    fun showAllContent() {
        binding.audioplayerContent.visibility = View.VISIBLE
        binding.bottomSheet.visibility = View.VISIBLE
    }

    private fun renderAddedToast(trackAdded: AddedTrackStatus){
        var displayText = ""
        if (trackAdded.trackAdded)
            displayText = "${getString(R.string.added_to_playlist)} ${trackAdded.playlistName}"
        else
            displayText = "${getString(R.string.already_added_to_playlist)} ${trackAdded.playlistName}"
        val snackbar = Snackbar.make(binding.overlay, displayText, R.layout.toast_layout)
        snackbar.duration = Snackbar.LENGTH_LONG
        snackbar.setTextColor(this.getColor(R.color.white_black))
        val view = snackbar.view
        val text = view.findViewById<TextView>(R.id.snackbar_text)
        text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        text.text = displayText
        snackbar.show()

    }
    private fun renderPlaylistList(playlistList: List<Playlist>) {
        playlistListAdapter.setPlaylistList(playlistList.toMutableList())
        playlistListAdapter.notifyDataSetChanged()
    }

    private fun changeLikeColor(isFavourite: Boolean) {
        if (isFavourite)
            binding.audioplayerRightButton.background =
                getDrawable(R.drawable.audioplayer_right_button_active)
        else
            binding.audioplayerRightButton.background =
                getDrawable(R.drawable.audioplayer_right_button)
    }

    private fun changeTimer(playStatus: PlayStatus) {
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

    private fun initializeActivityWithTrackInfo() {
        val trackJson = intent.getStringExtra(TRACK_JSON)
        val currentTrack: Track = Json.decodeFromString<Track>(trackJson!!)
        val trackIsFavourite = viewModel.checkFavourite()
        binding.trackName.text = currentTrack.trackName
        binding.trackAuthor.text = currentTrack.artistName
        binding.trackDurationText.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTrack.trackTime)
        binding.trackAlbumText.text = currentTrack.collectionName
        binding.trackYearText.text = currentTrack.releaseDate?.take(4)
        binding.trackGenreText.text = currentTrack.primaryGenreName
        binding.trackCountryText.text = currentTrack.country
        binding.trackAlbum.visibility = View.VISIBLE
        binding.trackAlbumText.visibility = View.VISIBLE
        if (currentTrack.isFavourite)
            binding.audioplayerRightButton.background =
                getDrawable(R.drawable.audioplayer_right_button_active)
        else
            binding.audioplayerRightButton.background =
                getDrawable(R.drawable.audioplayer_right_button)
        val infoTrackImage512 = currentTrack.artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
        val roundedCornersSize = 8
        Glide.with(binding.trackImage)
            .load(infoTrackImage512)
            .transform(RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(binding.trackImage)
        if (binding.trackAlbumText.text.isNullOrEmpty()) {
            binding.trackAlbumText.visibility = View.GONE
            binding.trackAlbum.visibility = View.GONE
        }
    }


    override fun onResume() {
        super.onResume()
        viewModel.checkPlaylists()
    }


    companion object {

        private const val TRACK_JSON = "trackJson"
        private const val NEWPLAYLIST_TAG = "NewPlaylistFragment"

    }
}