package com.practicum.playlistmaker.media.ui.fragment

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.favourites.ui.fragment.FavouritesFragment
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.activity.AudioplayerActivity
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MediaAdapter : RecyclerView.Adapter<MediaViewHolder> () {

    private var tracks: MutableList<Track> = mutableListOf()
    fun setTrackList(trackList: MutableList<Track>){
        tracks = trackList
    }
    private var isClickAllowed = true
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var parentFragment: FavouritesFragment

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        parentFragment = parent.findFragment()
        return MediaViewHolder(view)
    }


    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        holder.bind(tracks[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            if (clickDebounce()) {
                val displayIntent = Intent(context, AudioplayerActivity::class.java)
                displayIntent.putExtra("trackJson", Json.encodeToString(tracks[position]))
                context.startActivity(displayIntent)
            }
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
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