package com.practicum.playlistmaker.player.ui.activity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistListAdapter(
    private val onItemClick: ((playlist: Playlist) -> Unit)
) : RecyclerView.Adapter<PlaylistListViewHolder> () {

    private var playlists: MutableList<Playlist> = mutableListOf()
    fun setPlaylistList(playlistList: MutableList<Playlist>){
        playlists = playlistList
    }
    private var isClickAllowed = true
    private val scope = CoroutineScope(Dispatchers.Main.immediate)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist_horizontal_list_item, parent, false)
        return PlaylistListViewHolder(view)
    }


    override fun onBindViewHolder(holder: PlaylistListViewHolder, position: Int) {
        holder.bind(playlists[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
//                val displayIntent = Intent(context, AudioplayerActivity::class.java)
//                displayIntent.putExtra("trackJson", Json.encodeToString(tracks[position]))
//                context.startActivity(displayIntent)
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