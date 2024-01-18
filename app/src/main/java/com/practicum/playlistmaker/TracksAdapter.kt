package com.practicum.playlistmaker

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class TracksAdapter(
    private val tracks: List<Track>
) : RecyclerView.Adapter<TracksViewHolder> () {

    val searchHistory = SearchHistory()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        return TracksViewHolder(view)
    }

    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
        holder.bind(tracks[position])
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            searchHistory.addTrackToHistory(tracks[position])
            val displayIntent = Intent(context, AudioplayerActivity::class.java)
            displayIntent.putExtra("trackName", tracks[position].trackName)
            displayIntent.putExtra("artistName", tracks[position].artistName)
            displayIntent.putExtra("duration", tracks[position].trackTime)
            displayIntent.putExtra("artworkUrl100", tracks[position].artworkUrl100)
            displayIntent.putExtra("collectionName", tracks[position].collectionName)
            displayIntent.putExtra("releaseDate", tracks[position].releaseDate)
            displayIntent.putExtra("Genre", tracks[position].primaryGenreName)
            displayIntent.putExtra("country", tracks[position].country)
            context.startActivity(displayIntent)
            //Log.d("qciuibcubicicbnicw;ib", "${tracks[position].trackName} ${tracks[position].artistName} ${tracks[position].trackTime} ${tracks[position].artworkUrl100} \n ${tracks[position].collectionName} \n ${tracks[position].releaseDate} \n ${tracks[position].primaryGenreName} \n ${tracks[position].country}")
        }


    }

    override fun getItemCount(): Int {
        return tracks.size
    }

}