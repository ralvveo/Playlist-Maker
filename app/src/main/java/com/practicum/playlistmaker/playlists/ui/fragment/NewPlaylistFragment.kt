package com.practicum.playlistmaker.playlists.ui.fragment


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.player.ui.activity.AudioplayerActivity
import com.practicum.playlistmaker.playlists.domain.model.NewPlaylistState
import com.practicum.playlistmaker.playlists.ui.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.root.ui.activity.RootActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class NewPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel by viewModel<NewPlaylistViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createPlaylistButton.isEnabled = false

        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) viewModel.setName(s.toString())
                else viewModel.setName(null)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        val descrTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) viewModel.setDescr(s.toString())
                else viewModel.setDescr(null)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }

        binding.enterName.addTextChangedListener(nameTextWatcher)
        binding.enterDescr.addTextChangedListener(descrTextWatcher)

        viewModel.getState().observe(viewLifecycleOwner) { state ->
            render(state)

        }


        //регистрируем событие, которое вызывает photo picker
        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                //обрабатываем событие выбора пользователем фотографии
                if (uri != null) {
                    viewModel.setImage(uri)
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }


        binding.pictureButton.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }


        requireActivity().onBackPressedDispatcher.addCallback(backPressedCallback)
        backPressedCallback.isEnabled = true

    }


    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if ((binding.addPicture.visibility == View.GONE) or (binding.enterDescr.text.toString() != "") or (binding.enterName.text.toString() != ""))
                showAlert()
            else
                navigateBack()


        }
    }

    private fun navigateBack(){
        if (requireActivity() is RootActivity )
            findNavController().navigateUp()
        else {
            parentFragmentManager.beginTransaction()
                .remove(this)
                .commit()

            (requireActivity() as AudioplayerActivity).showAllContent()
        }
    }

    private fun showAlert() {
        MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.finish_playlist_creation) // Заголовок диалога
            .setMessage(R.string.all_data_will_be_lost) // Описание диалога
            .setNeutralButton(R.string.cancel) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setPositiveButton(R.string.finish) { dialog, which -> // Добавляет кнопку «Да»
                lifecycleScope.launch {
                    navigateBack()
                }

            }
            .show()
    }

    private fun render(state: NewPlaylistState) {
        when (state.newPlaylistName) {
            null -> {
                binding.enterName.background =
                    requireActivity().getDrawable(R.drawable.rounded_corner_shape)
                binding.createPlaylistButton.isEnabled = false
                binding.enterNameActiveText.visibility = View.GONE

            }

            else -> {
                binding.enterName.background =
                    requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
                binding.createPlaylistButton.isEnabled = true
                binding.enterNameActiveText.visibility = View.VISIBLE

            }
        }

        when (state.newPlaylistDescr) {
            null -> {
                binding.enterDescr.background =
                    requireActivity().getDrawable(R.drawable.rounded_corner_shape)
                binding.enterDescrActiveText.visibility = View.GONE

            }

            else -> {
                binding.enterDescr.background =
                    requireActivity().getDrawable(R.drawable.rounded_corner_shape_active)
                binding.enterDescrActiveText.visibility = View.VISIBLE

            }
        }

        when (state.newPlaylistImage) {
            null -> {


            }

            else -> {
                binding.pictureButton.setImageURI(state.newPlaylistImage)
                binding.pictureButton.background = null
                binding.addPicture.visibility = View.GONE
            }
        }




        binding.createPlaylistButton.setOnClickListener {
            viewModel.createPlaylist()
            showToast()
            navigateBack()

        }

        binding.newPlaylistArrowBack.setOnClickListener {
            if ((binding.addPicture.visibility == View.GONE) or (binding.enterDescr.text.toString() != "") or (binding.enterName.text.toString() != ""))
                showAlert()
            else
                navigateBack()
        }
    }

    private fun showToast() {
        val snackbar = Snackbar.make(
            binding.addPicture,
            "Плейлист ${binding.enterName.text} создан!",
            R.layout.toast_layout
        )
        snackbar.duration = Snackbar.LENGTH_LONG
        snackbar.setTextColor(requireActivity().getColor(R.color.white_black))
        val view = snackbar.view
        val text = view.findViewById<TextView>(R.id.snackbar_text)
        text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        text.text = "Плейлист ${binding.enterName.text} создан!"
        snackbar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.isEnabled = false

    }
}