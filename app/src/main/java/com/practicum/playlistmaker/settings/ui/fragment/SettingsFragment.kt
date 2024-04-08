package com.practicum.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

}