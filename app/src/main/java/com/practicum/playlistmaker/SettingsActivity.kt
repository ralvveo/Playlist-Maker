package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        //Кнопка Назад
        val settingsButtonBack = findViewById<ImageView>(R.id.settings_button_back)
        settingsButtonBack.setOnClickListener {

            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        //Кнопка Поделиться приложением
        val settingsButtonShare = findViewById<FrameLayout>(R.id.settings_button_share)
        settingsButtonShare.setOnClickListener {

            val shareMessage = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(shareIntent)
        }

        //Кнопка Написать в поддержку
        val settingsButtonSupport = findViewById<FrameLayout>(R.id.settings_button_support)
        settingsButtonSupport.setOnClickListener {

            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_message_address)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_message_theme))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message_text))
            startActivity(supportIntent)
        }

        //Кнопка Пользовательское соглашение
        val settingsButtonAssignment = findViewById<FrameLayout>(R.id.settings_button_assignment)
        settingsButtonAssignment.setOnClickListener {

            val settingsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.assignment_link)))
            startActivity(settingsIntent)
        }


    }
}