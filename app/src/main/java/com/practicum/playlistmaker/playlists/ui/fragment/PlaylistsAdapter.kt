package com.practicum.playlistmaker.playlists.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistsAdapter(
    private val onItemClick: ((playlist: Playlist) -> Unit)
) : RecyclerView.Adapter<PlaylistsViewHolder> () {

    private var playlists: MutableList<Playlist> = mutableListOf()
    fun setplaylistList(playlistList: MutableList<Playlist>){
        playlists = playlistList
    }
    private var isClickAllowed = true
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var parentFragment: PlaylistsFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        parentFragment = parent.findFragment()
        return PlaylistsViewHolder(view)
    }


    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                onItemClick(playlists[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            scope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}