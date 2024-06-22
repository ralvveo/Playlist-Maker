package com.practicum.playlistmaker.playlist.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsDbConverter
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.model.PlaylistSerializable
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistBinding
    private lateinit var currentPlaylist: Playlist
    private lateinit var trackAdapter: TracksAdapter
    private val viewModel by viewModel <PlaylistViewModel>{
        parametersOf(currentPlaylist)
    }
    private var tracksDurationSum = 0L
    private var currentTrackList = mutableListOf<Track>()

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

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        trackAdapter = TracksAdapter(this::showDeleteDialog)
        binding.recyclerView.adapter = trackAdapter

        binding.playlistArrowBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.shareIcon.setOnClickListener {
            if (currentPlaylist.playlistTrackCount == "0"){
                showShareErrorDialog()
            }
            else{
                sharePlaylist()
            }
        }

        binding.buttonShareBottomSheet.setOnClickListener {
            if (currentPlaylist.playlistTrackCount == "0"){
                showShareErrorDialog()
            }
            else{
                sharePlaylist()
            }
        }

        binding.buttonDeleteBottomSheet.setOnClickListener {
            showDeletePlaylistDialog()
        }

        binding.buttonChangeBottomSheet.setOnClickListener {
            findNavController().navigate(R.id.action_playlistFragment_to_newPlaylistFragment, NewPlaylistFragment.createArgs(Json.encodeToString(converter.serializeMap(currentPlaylist))))
        }


        val bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet2).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
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

        binding.moreIcon.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun sharePlaylist() {
        val shareMessage = generateShareMessage()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        shareIntent.setType("text/plain")
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        requireActivity().startActivity(shareIntent)
    }

    private fun generateShareMessage(): String{
        var resultMessage = ""
        resultMessage += requireActivity().resources.getQuantityString(R.plurals.plurals_tracks, currentPlaylist.playlistTrackCount.toInt(), currentPlaylist.playlistTrackCount.toInt())
        resultMessage += "\n"
        for (track in currentTrackList){
            resultMessage += "${currentTrackList.indexOf(track)+1}. ${track.artistName} - ${track.trackName} (${SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTime)})\n"
        }
        return resultMessage
    }

    private fun showShareErrorDialog(){
        val displayText = "${requireActivity().getString(R.string.nothing_to_share)}"
        val snackbar = Snackbar.make(binding.overlay, displayText, R.layout.toast_layout)
        snackbar.duration = Snackbar.LENGTH_LONG
        snackbar.setTextColor(requireActivity().getColor(R.color.white_black))
        val view = snackbar.view
        val text = view.findViewById<TextView>(R.id.snackbar_text)
        text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        text.text = displayText
        snackbar.show()
    }

    private fun showDeleteDialog(track: Track){
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle(R.string.wanna_delete_track) // Заголовок диалога
            //.setMessage() // Описание диалога
            .setNeutralButton(R.string.no) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.yes) { dialog, which -> // Добавляет кнопку «Да»
                viewModel.deleteTrackFromPlaylist(track)
            }
            .show()
    }

    private fun showDeletePlaylistDialog(){
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog2)
            .setTitle("${getString(R.string.wanna_delete_playlist)} «${currentPlaylist.playlistName}»?") // Заголовок диалога
            //.setMessage() // Описание диалога
            .setNeutralButton(R.string.no) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.yes) { dialog, which -> // Добавляет кнопку «Да»
                viewModel.deletePlaylist(currentPlaylist)
                findNavController().navigateUp()
            }
            .show()
    }

    private fun render(trackList: MutableList<Track>){
        trackAdapter.setTrackList(trackList)
        binding.playlistTrackCount.text = requireActivity().resources.getQuantityString(R.plurals.plurals_tracks, trackList.size, trackList.size)
        trackAdapter.notifyDataSetChanged()
        tracksDurationSum = 0
        for (track in trackList)
            tracksDurationSum += track.trackTime
        val tracksDurationSumString = SimpleDateFormat("m", Locale.getDefault()).format(tracksDurationSum)
        binding.playlistLength.text = requireActivity().resources.getQuantityString(R.plurals.plurals_minutes, tracksDurationSumString.toInt(), tracksDurationSumString.toInt())
        currentTrackList = trackList


    }
    private fun initializePlaylistFragment(playlist: Playlist){
        binding.playlistName.text = playlist.playlistName
        binding.playlistYear.text = playlist.playlistDescr
        binding.playlistTrackCount.text = requireActivity().resources.getQuantityString(R.plurals.plurals_tracks, playlist.playlistTrackCount.toInt(), playlist.playlistTrackCount.toInt())

        binding.playlistNameBottomSheet.text = playlist.playlistName
        binding.playlistTrackCountBottomSheet.text = requireActivity().resources.getQuantityString(R.plurals.plurals_tracks, playlist.playlistTrackCount.toInt(), playlist.playlistTrackCount.toInt())
        val fileCodeName = playlist.playlistImage.toString().takeLast(10)
        val filePath = File(requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), MY_ALBUM)
        val file = File(filePath, "$fileCodeName.jpg")
        val roundedCornersSize1 = 8
        val roundedCornersSize2 = 2
        Glide.with(binding.playlistLength)
            .load(file.toUri())
            .transform(CenterCrop(), RoundedCorners(roundedCornersSize1))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(binding.playlistImage)

        Glide.with(binding.playlistLength)
            .load(file.toUri())
            .transform(CenterCrop(), RoundedCorners(roundedCornersSize2))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(binding.playlistImageBottomSheet)
    }

    override fun onResume() {
        super.onResume()
        val playlistJson = requireArguments().getString(PLAYLIST_JSON)
        val converter = PlaylistsDbConverter()
        currentPlaylist = converter.serializeMap(Json.decodeFromString<PlaylistSerializable>(playlistJson?: ""))
        initializePlaylistFragment(currentPlaylist)
    }

    companion object{

        const val PLAYLIST_JSON = "playlist_json"
        const val MY_ALBUM = "myalbum"

        fun createArgs(playlistJson: String): Bundle =
            bundleOf(PLAYLIST_JSON to playlistJson)
    }
}