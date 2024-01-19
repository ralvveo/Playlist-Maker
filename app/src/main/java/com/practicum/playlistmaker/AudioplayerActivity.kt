package com.practicum.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity() : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audioplayer)

        //Get Track info
        val infoTrackName = intent.getStringExtra("trackName")
        val infoTrackArtistName = intent.getStringExtra("artistName")
        val infoTrackDuration = intent.getLongExtra("duration",0)
        val infoTrackImage100 = intent.getStringExtra("artworkUrl100")
        val infoTrackCollectionName = intent.getStringExtra("collectionName")
        val infoTrackReleaseDate = intent.getStringExtra("releaseDate")
        val infoTrackGenre = intent.getStringExtra("Genre")
        val infoTrackCountry = intent.getStringExtra("country")

        //Initialize track Info
        val trackName = findViewById<TextView>(R.id.track_name)
        val trackArtistName = findViewById<TextView>(R.id.track_author)
        val trackImage = findViewById<ImageView>(R.id.track_image)
        val trackDuration = findViewById<TextView>(R.id.track_duration_text)
        val trackAlbum = findViewById<TextView>(R.id.track_album_text)
        val trackYear= findViewById<TextView>(R.id.track_year_text)
        val trackGenre = findViewById<TextView>(R.id.track_genre_text)
        val trackCountry = findViewById<TextView>(R.id.track_country_text)

        val trackAlbumLeft = findViewById<TextView>(R.id.track_album)

        trackName.text = infoTrackName
        trackArtistName.text = infoTrackArtistName
        trackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(infoTrackDuration)
        trackAlbum.text = infoTrackCollectionName
        trackYear.text = infoTrackReleaseDate?.take(4)
        trackGenre.text = infoTrackGenre
        trackCountry.text = infoTrackCountry

        trackAlbumLeft.visibility = View.VISIBLE
        trackAlbum.visibility = View.VISIBLE


        val infoTrackImage512 = infoTrackImage100?.replaceAfterLast('/',"512x512bb.jpg")
        val roundedCornersSize: Int = 8
        Glide.with(trackImage)
            .load(infoTrackImage512)
            .transform(RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(trackImage)

        //Кнопка Назад
        val audioplayerButtonBack = findViewById<ImageButton>(R.id.audioplayer_arrow_back)
        audioplayerButtonBack.setOnClickListener {
            finish()
        }
        if (trackAlbum.text.isNullOrEmpty()){
            trackAlbum.visibility = View.GONE
            trackAlbumLeft.visibility = View.GONE
        }
    }


}