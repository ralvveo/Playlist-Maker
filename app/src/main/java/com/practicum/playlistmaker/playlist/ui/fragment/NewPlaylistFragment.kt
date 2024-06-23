package com.practicum.playlistmaker.playlist.ui.fragment


import android.os.Bundle
import android.os.Environment
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
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentNewPlaylistBinding
import com.practicum.playlistmaker.player.ui.activity.AudioplayerActivity
import com.practicum.playlistmaker.playlist.ui.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.playlists.data.converters.PlaylistsDbConverter
import com.practicum.playlistmaker.playlists.domain.model.NewPlaylistState
import com.practicum.playlistmaker.playlists.domain.model.Playlist
import com.practicum.playlistmaker.playlists.domain.model.PlaylistSerializable
import com.practicum.playlistmaker.root.ui.activity.RootActivity
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File


class NewPlaylistFragment : Fragment() {

    private lateinit var binding: FragmentNewPlaylistBinding
    private val viewModel by viewModel<NewPlaylistViewModel>()
    private var currentPlaylist: Playlist? = null
    private var playlistJson: String? = null
    private var changeFlag = false

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

        playlistJson = arguments?.getString(PLAYLIST_JSON)
        val converter = PlaylistsDbConverter()
        if (playlistJson != null) {
            currentPlaylist = converter.serializeMap(
                Json.decodeFromString<PlaylistSerializable>(
                    playlistJson ?: ""
                )
            )
            changeFlag = true
        }
        initializePlaylistFragment(currentPlaylist)

        val nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    viewModel.setName(s.toString())
                    currentPlaylist?.playlistName = s.toString()
                }
                else viewModel.setName(null)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        val descrTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    viewModel.setDescr(s.toString())
                    currentPlaylist?.playlistDescr = s.toString()

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
                    currentPlaylist?.playlistImage = uri
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

    private fun initializePlaylistFragment(playlist: Playlist?){

        if (playlist != null) {
            val fileCodeName = playlist.playlistImage.toString().takeLast(10)
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                PlaylistFragment.MY_ALBUM
            )
            val file = File(filePath, "$fileCodeName.jpg")
            val roundedCornersSize1 = 8
            binding.addPicture.visibility = View.GONE
            Glide.with(binding.enterName)
                .load(file.toUri())
                .transform(CenterCrop(), RoundedCorners(roundedCornersSize1))
                .placeholder(R.drawable.audioplayer_placeholder)
                .into(binding.pictureButton)


            binding.newPlaylistTitle.text = getString(R.string.change)
            binding.createPlaylistButtonText.text = getString(R.string.save)
            binding.enterName.setText(playlist.playlistName)
            binding.enterDescr.setText(playlist.playlistDescr)

            viewModel.setName(playlist.playlistName)
            viewModel.setDescr(playlist.playlistDescr)

        }
    }


    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!changeFlag) {
                if ((binding.addPicture.visibility == View.GONE) or (binding.enterDescr.text.toString() != "") or (binding.enterName.text.toString() != ""))
                    showAlert()
                else
                    navigateBack()
            }
            else navigateBack()


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
        MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog)
            .setTitle(R.string.finish_playlist_creation) // Заголовок диалога
            .setMessage(R.string.all_data_will_be_lost) // Описание диалога
            .setNeutralButton(R.string.cancel) { dialog, which -> // Добавляет кнопку «Отмена»
                // Действия, выполняемые при нажатии на кнопку «Отмена»
            }

            .setNegativeButton(R.string.finish) { dialog, which -> // Добавляет кнопку «Да»
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
            null, "" -> {
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

            if (!changeFlag) {
                viewModel.createPlaylist()
                showCreateToast()
                navigateBack()
            }
            else{
                viewModel.savePlaylist(currentPlaylist!!)
                showSaveToast()
                findNavController().popBackStack()
                navigateBack()
                val converter = PlaylistsDbConverter()
                findNavController().navigate(R.id.action_mediaFragment_to_playlistFragment, PlaylistFragment.createArgs(Json.encodeToString(converter.serializeMap(currentPlaylist!!))))
            }

        }

        binding.newPlaylistArrowBack.setOnClickListener {
            if (!changeFlag) {
                if ((binding.addPicture.visibility == View.GONE) or (binding.enterDescr.text.toString() != "") or (binding.enterName.text.toString() != ""))
                    showAlert()
                else
                    navigateBack()
            }
            else navigateBack()
        }
    }

    private fun showCreateToast() {
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

    private fun showSaveToast() {
        val snackbar = Snackbar.make(
            binding.addPicture,
            "Плейлист ${binding.enterName.text} изменён!",
            R.layout.toast_layout
        )
        snackbar.duration = Snackbar.LENGTH_LONG
        snackbar.setTextColor(requireActivity().getColor(R.color.white_black))
        val view = snackbar.view
        val text = view.findViewById<TextView>(R.id.snackbar_text)
        text.textAlignment = View.TEXT_ALIGNMENT_CENTER
        text.text = "Плейлист ${binding.enterName.text} изменён!"
        snackbar.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        backPressedCallback.isEnabled = false

    }

    companion object{
        const val PLAYLIST_JSON = "playlist_json"
        fun createArgs(playlistJson: String): Bundle =
            bundleOf(PLAYLIST_JSON to playlistJson)
    }
}