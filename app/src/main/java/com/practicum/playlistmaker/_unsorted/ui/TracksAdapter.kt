package com.practicum.playlistmaker._unsorted.ui


import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker._unsorted.creator.Creator
import com.practicum.playlistmaker._unsorted.ui.SearchActivity.Companion.CLICK_DEBOUNCE_DELAY
import com.practicum.playlistmaker._unsorted_domain.model.Track
import com.practicum.playlistmaker._unsorted_domain.repository.SearchHistoryFunctions
import kotlinx.serialization.*
import kotlinx.serialization.json.Json

class TracksAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TracksViewHolder> () {

    private var isClickAllowed = true
    private lateinit var searchHistory: SearchHistoryFunctions
    private val handler = Handler(Looper.getMainLooper())

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
            searchHistory = Creator.provideSearchHistoryFunctions(null) //Тут экземлпяр SharedPrefs не требуется, не происходит работы с ними, поэтому null!
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
            holder.bind(tracks[position])
            val context = holder.itemView.context
            holder.itemView.setOnClickListener {
                if (clickDebounce()) {
                    val displayIntent = Intent(context, AudioplayerActivity::class.java)
                    displayIntent.putExtra("trackJson", Json.encodeToString(tracks[position]))
                    context.startActivity(displayIntent)
                    searchHistory.addTrackToHistory(tracks[position])
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
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}