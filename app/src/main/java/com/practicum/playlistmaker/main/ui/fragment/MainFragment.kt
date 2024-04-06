package com.practicum.playlistmaker.main.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.media.ui.fragment.MediaFragment
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import com.practicum.playlistmaker.settings.ui.fragment.SettingsFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        viewModel.initialize()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.searchButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootFragmentContainerView, SearchFragment())
                .addToBackStack("SearchFragment")
                .commit()
        }

        binding.mediaButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootFragmentContainerView, MediaFragment())
                .addToBackStack("MediaFragment")
                .commit()
        }

        binding.settingsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.rootFragmentContainerView, SettingsFragment())
                .addToBackStack("SettingsFragment")
                .commit()
        }
    }
}