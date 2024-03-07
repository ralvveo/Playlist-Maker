package com.practicum.playlistmaker.settings.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SettingsViewModel.factory(context = this))[SettingsViewModel::class.java]
        //Кнопка Назад
        binding.settingsButtonBack.setOnClickListener {
            finish()
        }
        binding.themeSwitcher.isChecked = viewModel.getTheme()
        //Кнопка Поделиться приложением
        binding.settingsButtonShare.setOnClickListener {
            viewModel.shareApp()
        }

        //Кнопка Написать в поддержку
        binding.settingsButtonSupport.setOnClickListener {
            viewModel.openSupport()
        }

        //Кнопка Пользовательское соглашение
        binding.settingsButtonAssignment.setOnClickListener {
            viewModel.openTerms()
        }

        //Переключатель темы
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.switchTheme()
        }

    }
    //Фикс бага с мигающим экраном при смене темы
    override fun recreate() {
        finish()
        startActivity(intent)
        overridePendingTransition(
            R.anim.empty_animation,
            R.anim.empty_animation
        )
    }
}