package com.practicum.playlistmaker


import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.SearchActivity.Companion.CLICK_DEBOUNCE_DELAY


class TracksAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TracksViewHolder> () {

    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val searchHistory = SearchHistory()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {

            holder.bind(tracks[position])
            val context = holder.itemView.context
            holder.itemView.setOnClickListener {
                if (clickDebounce()) {
                    val displayIntent = Intent(context, AudioplayerActivity::class.java)
                    displayIntent.putExtra("trackName", tracks[position].trackName)
                    displayIntent.putExtra("artistName", tracks[position].artistName)
                    displayIntent.putExtra("duration", tracks[position].trackTime)
                    displayIntent.putExtra("artworkUrl100", tracks[position].artworkUrl100)
                    displayIntent.putExtra("collectionName", tracks[position].collectionName)
                    displayIntent.putExtra("releaseDate", tracks[position].releaseDate)
                    displayIntent.putExtra("Genre", tracks[position].primaryGenreName)
                    displayIntent.putExtra("country", tracks[position].country)
                    displayIntent.putExtra("previewUrl", tracks[position].previewUrl)
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