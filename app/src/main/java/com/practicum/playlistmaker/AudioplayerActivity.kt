package com.practicum.playlistmaker

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import java.text.SimpleDateFormat
import java.util.Locale

class AudioplayerActivity() : AppCompatActivity() {

    //Все состояния медиаплеера
    companion object{
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val DELAY = 500L
    }

    private lateinit var binding: ActivityAudioplayerBinding

    //Начальное состояние медиаплеера
    private var playerState = STATE_DEFAULT
    private var handler = Handler(Looper.getMainLooper())
    private var mediaPlayer = MediaPlayer()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        preparePlayer()


        //Get Track info
        val infoTrackName = intent.getStringExtra("trackName")
        val infoTrackArtistName = intent.getStringExtra("artistName")
        val infoTrackDuration = intent.getLongExtra("duration",0)
        val infoTrackImage100 = intent.getStringExtra("artworkUrl100")
        val infoTrackCollectionName = intent.getStringExtra("collectionName")
        val infoTrackReleaseDate = intent.getStringExtra("releaseDate")
        val infoTrackGenre = intent.getStringExtra("Genre")
        val infoTrackCountry = intent.getStringExtra("country")



        binding.trackName.text = infoTrackName
        binding.trackAuthor.text = infoTrackArtistName
        binding.trackDurationText.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(infoTrackDuration)
        binding.trackAlbumText.text = infoTrackCollectionName
        binding.trackYearText.text = infoTrackReleaseDate?.take(4)
        binding.trackGenreText.text = infoTrackGenre
        binding.trackCountryText.text = infoTrackCountry
        binding.trackAlbum.visibility = View.VISIBLE
        binding.trackAlbumText.visibility = View.VISIBLE


        val infoTrackImage512 = infoTrackImage100?.replaceAfterLast('/',"512x512bb.jpg")
        val roundedCornersSize = 8
        Glide.with(binding.trackImage)
            .load(infoTrackImage512)
            .transform(RoundedCorners(roundedCornersSize))
            .placeholder(R.drawable.audioplayer_placeholder)
            .into(binding.trackImage)

        //Кнопка Назад
        binding.audioplayerArrowBack.setOnClickListener {
            finish()

        }

        //Кнопка Воспроизведения
        binding.audioplayerCenterButton.setOnClickListener{
            playbackControl()
        }



        if (binding.trackAlbumText.text.isNullOrEmpty()){
            binding.trackAlbumText.visibility = View.GONE
            binding.trackAlbum.visibility = View.GONE
        }





    }

    private fun startTimer(){
        handler.post(createUpdateTimerTask())
    }

    private fun createUpdateTimerTask() : Runnable{
        return object: Runnable{
            override fun run() {
                if (playerState == STATE_PLAYING){
                    val elapsedTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    if (mediaPlayer.currentPosition < 30000){
                        binding.trackCurrentTime.text = elapsedTime
                        handler.postDelayed(this, DELAY)
                    }
                    else{
                        binding.trackCurrentTime.text = "00:00"
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {

        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()

            }
        }
    }

    private fun preparePlayer() {
        val previewUrl = intent.getStringExtra("previewUrl")
        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            binding.audioplayerCenterButton.isEnabled = true
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            binding.trackCurrentTime.text = "00:00"
            binding.audioplayerCenterButton.background = getDrawable(R.drawable.audioplayer_center_button_play)
            handler.removeCallbacksAndMessages(createUpdateTimerTask())
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        startTimer()
        playerState = STATE_PLAYING
        binding.audioplayerCenterButton.background = getDrawable(R.drawable.audioplayer_center_button_pause)
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        handler.removeCallbacks(createUpdateTimerTask())
        playerState = STATE_PAUSED
        binding.audioplayerCenterButton.background = getDrawable(R.drawable.audioplayer_center_button_play)
    }








}