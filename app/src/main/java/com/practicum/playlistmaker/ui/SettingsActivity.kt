package com.practicum.playlistmaker.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.DARK_THEME_INDICATOR
import com.practicum.playlistmaker.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        binding.themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)

        //Кнопка Назад
        binding.settingsButtonBack.setOnClickListener {
            finish()
        }

        //Кнопка Поделиться приложением
        binding.settingsButtonShare.setOnClickListener {
            val shareMessage = getString(R.string.share_link)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(shareIntent)
        }

        //Кнопка Написать в поддержку
        binding.settingsButtonSupport.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.support_message_address)))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.support_message_theme))
            supportIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.support_message_text))
            startActivity(supportIntent)
        }

        //Кнопка Пользовательское соглашение
        binding.settingsButtonAssignment.setOnClickListener {
            val settingsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.assignment_link)))
            startActivity(settingsIntent)
        }

        //Переключатель темы
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)


        }

        binding.themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)

    }

    override fun onResume() {
        super.onResume()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        binding.themeSwitcher.isChecked = sharedPrefs.getBoolean(DARK_THEME_INDICATOR, false)
    }

    //Фикс бага с мигающим экраном при смене темы
    override fun recreate() {
        finish()
        overridePendingTransition(
            R.anim.empty_animation,
            R.anim.empty_animation
        )
        startActivity(intent)
        overridePendingTransition(
            R.anim.empty_animation,
            R.anim.empty_animation
        )
    }
}