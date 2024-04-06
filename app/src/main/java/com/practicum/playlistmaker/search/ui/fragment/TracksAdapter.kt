package com.practicum.playlistmaker.search.ui.fragment


import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.fragment.AudioplayerFragment
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY
import kotlinx.serialization.*
import kotlinx.serialization.json.Json


class TracksAdapter(
    private val onItemClick: ((track: Track) -> Unit)
) : RecyclerView.Adapter<TracksViewHolder> () {

    private var tracks: MutableList<Track> = mutableListOf()
    fun setTrackList(trackList: MutableList<Track>){
        tracks = trackList
    }
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var parentFragment: SearchFragment

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TracksViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
            parentFragment = parent.findFragment()
        return TracksViewHolder(view)
    }


    override fun onBindViewHolder(holder: TracksViewHolder, position: Int) {
            holder.bind(tracks[position])
            val context = holder.itemView.context
            holder.itemView.setOnClickListener {
                if (clickDebounce()) {
                    parentFragment.parentFragmentManager.beginTransaction()
                        .replace(R.id.rootFragmentContainerView, AudioplayerFragment.newInstance(Json.encodeToString(tracks[position])))
                        .addToBackStack("AudioplayerFragment")
                        .commit()
//                    val displayIntent = Intent(context, AudioplayerActivity::class.java)
//                    displayIntent.putExtra("trackJson", Json.encodeToString(tracks[position]))
//                    context.startActivity(displayIntent)
                    onItemClick(tracks[position])
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