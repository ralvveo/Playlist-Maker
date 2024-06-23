package com.practicum.playlistmaker.root.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.player.ui.activity.AudioplayerActivity
import com.practicum.playlistmaker.root.ui.view_model.RootViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private val viewModel by viewModel<RootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.initialize()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.audioplayerActivity, R.id.newPlaylistFragment, R.id.playlistFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }






    }


}