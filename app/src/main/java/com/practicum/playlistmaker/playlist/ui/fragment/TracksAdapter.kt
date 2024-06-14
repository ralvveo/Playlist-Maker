package com.practicum.playlistmaker.playlist.ui.fragment


import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.activity.AudioplayerActivity
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


class TracksAdapter : RecyclerView.Adapter<TracksViewHolder> () {

    private var tracks: MutableList<Track> = mutableListOf()
    fun setTrackList(trackList: MutableList<Track>){
        tracks = trackList
    }
    private var isClickAllowed = true
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private lateinit var parentFragment: PlaylistFragment

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
            parentFragment = parent.findFragment()
        return TracksViewHolder(view)
    }


    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
            holder.bind(tracks[position])
            val context = holder.itemView.context
            repeat(10){Log.d("Context", "$context")}
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
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}