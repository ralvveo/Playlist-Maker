package com.practicum.playlistmaker.playlists.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment : Fragment() {

    private lateinit var binding: FragmentPlaylistsBinding
    private lateinit var playlistsAdapter: PlaylistsAdapter
    private val viewModel by viewModel<PlaylistsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.newPlaylistButton.setOnClickListener{
            findNavController().navigate(R.id.action_mediaFragment_to_newPlaylistFragment)
        }
        playlistsAdapter = PlaylistsAdapter()

        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2,LinearLayoutManager.VERTICAL, true)
        binding.recyclerView.adapter = playlistsAdapter

        viewModel.getState().observe(viewLifecycleOwner){state -> render(state)

        }

    }

    private fun render(state: MutableList<Playlist>){
        if (state.isEmpty())
            showEmptyState()
        else
            showPlaylists(state)
    }

    private fun showEmptyState(){
        binding.recyclerView.visibility = View.GONE
        binding.favouritesClearText.visibility = View.VISIBLE
        binding.favouritesClearImage.visibility = View.VISIBLE
    }

    private fun showPlaylists(state: MutableList<Playlist>){
        binding.recyclerView.visibility = View.VISIBLE
        binding.favouritesClearText.visibility = View.GONE
        binding.favouritesClearImage.visibility = View.GONE
        playlistsAdapter.setplaylistList(state)
        playlistsAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        viewModel.read()
    }
}