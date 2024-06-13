package com.practicum.playlistmaker.playlist.ui.fragment

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.playlists.data.converters.CorrectEnding
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsDbConverter
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.model.PlaylistSerializable
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var currentPlaylist: Playlist
    private lateinit var trackAdapter: TracksAdapter
    private val viewModel by viewModel <PlaylistViewModel>{
        parametersOf(currentPlaylist)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val playlistJson = requireArguments().getString(PLAYLIST_JSON)
        val converter = PlaylistsDbConverter()
        currentPlaylist = converter.serializeMap(Json.decodeFromString<PlaylistSerializable>(playlistJson?: ""))
        initializePlaylistFragment(currentPlaylist)

        viewModel.getState().observe(viewLifecycleOwner){trackList ->
            render(trackList)

        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trackAdapter = TracksAdapter()
    }

    private fun render(trackList: MutableList<Track>){
        //Будет доделано в 23 спринте
//        trackAdapter.setTrackList(trackList)
//        trackAdapter.notifyDataSetChanged()
    }
    private fun initializePlaylistFragment(playlist: Playlist){
        binding.playlistName.text = playlist.playlistName
        binding.playlistYear.text = "2022"
        binding.playlistLength.text = "300 минут"
        binding.playlistTrackCount.text = "${playlist.playlistTrackCount} ${CorrectEnding.doEnding(playlist.playlistTrackCount.toInt())}"

        val fileCodeName = playlist.playlistImage.toString().takeLast(10)
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_ALBUM)
        val file = File(filePath, "$fileCodeName.jpg")
        val roundedCornersSize = 8
        Glide.with(binding.playlistLength)
            .load(file.toUri())
            .transform(CenterCrop(), RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(binding.playlistImage)
    }


    companion object{

        const val PLAYLIST_JSON = "playlist_json"
        const val MY_ALBUM = "myalbum"

        fun createArgs(playlistJson: String): Bundle =
            bundleOf(PLAYLIST_JSON to playlistJson)
    }
}