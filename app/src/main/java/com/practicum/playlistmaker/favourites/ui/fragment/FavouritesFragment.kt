package com.practicum.playlistmaker.favourites.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.databinding.FragmentFavouritesBinding
import com.practicum.playlistmaker.favourites.domain.state.MediaState
import com.practicum.playlistmaker.favourites.ui.view_model.FavouritesViewModel
import com.practicum.playlistmaker.media.ui.fragment.MediaAdapter
import com.practicum.playlistmaker.player.domain.model.Track
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {

    private lateinit var binding: FragmentFavouritesBinding
    private lateinit var mediaAdapter: MediaAdapter
    private val viewModel by viewModel<FavouritesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaAdapter = MediaAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)
        binding.recyclerView.adapter = mediaAdapter

        viewModel.getStateLiveData().observe(viewLifecycleOwner){state ->
            render(state)
        }

    }

    override fun onResume() {
        super.onResume()
        viewModel.checkFavouriteTracks()
    }
    private fun render(state: MediaState){
        when (state){
            is MediaState.NoFavourite -> showPlaceholder()
            is MediaState.MediaContent -> showMediaContent(state.mediaList)
        }
    }

    private fun showPlaceholder(){
        binding.favouritesClearImage.visibility = View.VISIBLE
        binding.favouritesClearText.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
    }


    private fun showMediaContent(trackList: List<Track>){
        mediaAdapter.setTrackList(trackList.toMutableList())
        mediaAdapter.notifyDataSetChanged()
        binding.favouritesClearImage.visibility = View.GONE
        binding.favouritesClearText.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

}