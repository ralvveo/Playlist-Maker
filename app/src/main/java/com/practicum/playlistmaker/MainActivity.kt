package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        searchButton.setOnClickListener {

            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }


        val mediaButton = findViewById<Button>(R.id.media_button)
        mediaButton.setOnClickListener {

            val displayIntent = Intent(this, MediaActivity::class.java)
            startActivity(displayIntent)
        }


        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {

            val displayIntent = Intent(this, SettingsActivity::class.java)
            startActivity(displayIntent)
        }



        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.resources.displayMetrics).toInt()
        }
        val cornerTrackImageSize = dpToPx(2f, this)

    }

}
