package com.practicum.playlistmaker.search.ui.activity

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.data.model.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel
    private lateinit var trackHistoryAdapter: TracksAdapter
    private lateinit var trackAdapter: TracksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this, SearchViewModel.factory(this))[SearchViewModel::class.java]
        trackHistoryAdapter = TracksAdapter(viewModel::addTrackToHistory)
        binding.searchHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trackAdapter = TracksAdapter(viewModel::addTrackToHistory)
        binding.recyclerView.adapter = trackAdapter

        viewModel.getSearchHistoryLiveData().observe(this){ trackHistoryList ->
            updateHistoryList(trackHistoryList)
        }

        viewModel.getState().observe(this){state ->
            render(state)
        }

        viewModel.getSearchLiveData().observe(this){searchList ->
            render(SearchState.SearchContent(searchList))
        }

        //Кнопка Назад
        binding.searchButtonBack.setOnClickListener {
            finishAfterTransition()
        }

        binding.clearButton.setOnClickListener {
            binding.inputEditText.setText("")
            viewModel.clearSearchButton()
        }

        //Видимость Крестика в Поиске
        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(binding.clearButton.windowToken, 0)
                View.GONE

            } else {
                View.VISIBLE
            }
        }

        //Видимость Истории Поиска
        binding.inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && binding.inputEditText.text.isEmpty() && !viewModel.getSearchHistoryLiveData().value.isNullOrEmpty()) {
                viewModel.stateHistoryContent()
            }
            else {
                viewModel.stateSearchContent()
            }
        }

        binding.searchHistoryButton.setOnClickListener{
            viewModel.clearHistory()
        }

        //Text Watcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(binding.inputEditText.text.toString())
                binding.clearButton.visibility = clearButtonVisibility(s)
                if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && !viewModel.getSearchHistoryLiveData().value.isNullOrEmpty())
                    viewModel.stateHistoryContent()
                else
                    viewModel.stateSearchContent()
                trackHistoryAdapter.notifyDataSetChanged()
                if (s?.isEmpty() == true){
                    viewModel.clearSearchButton()
                }
            }
            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)
        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                viewModel.goForApiSearch(binding.inputEditText.text.toString())
            false
        }

        binding.searchErrorButton.setOnClickListener {
            viewModel.goForApiSearch(binding.inputEditText.text.toString())
        }

    }


    private fun render(state: SearchState){
        when (state){
             is SearchState.SearchContent -> showSearchList(state.searchList)
             is SearchState.NoInternet -> showNoInternetPlaceholder()
             is SearchState.NothingFound -> showNothingFoundPlaceholder()
             is SearchState.Loading -> showLoading()
             is SearchState.HistoryContent -> showHistoryList(state.historyList.toMutableList())
        }
    }


    private fun updateHistoryList(trackHistoryList: MutableList<Track>?){
        if (!trackHistoryList.isNullOrEmpty()) {
            trackHistoryAdapter = TracksAdapter(viewModel::addTrackToHistory)
            trackHistoryAdapter.setTrackList(trackHistoryList ?: mutableListOf())
            binding.searchHistoryList.adapter = trackHistoryAdapter
            trackHistoryAdapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        }
        else binding.searchHistory.visibility = View.GONE

    }

    private fun showHistoryList(trackHistoryList: MutableList<Track>?){
        if (!trackHistoryList.isNullOrEmpty()) {
            trackHistoryAdapter = TracksAdapter(viewModel::addTrackToHistory)
            trackHistoryAdapter.setTrackList(trackHistoryList ?: mutableListOf())
            binding.searchHistoryList.adapter = trackHistoryAdapter
            binding.searchHistory.visibility = View.VISIBLE
            binding.searchError.visibility = View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
            binding.progressBar.visibility = View.GONE
        }
        else binding.searchHistory.visibility = View.GONE

    }

    private fun showSearchList(trackList: List<Track>){
        trackAdapter = TracksAdapter(viewModel::addTrackToHistory)
        trackAdapter.setTrackList(trackList.toMutableList())
        binding.recyclerView.adapter = trackAdapter
        trackAdapter.notifyDataSetChanged()
        binding.recyclerView.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
        binding.searchError.visibility = View.GONE
        binding.searchHistory.visibility = View.GONE
    }

    private fun showNoInternetPlaceholder(){
        binding.progressBar.visibility = View.GONE
        binding.searchError.visibility = View.VISIBLE
        binding.searchErrorButton.visibility = View.VISIBLE
        binding.searchErrorText.setText(R.string.no_internet_text)
        binding.searchErrorImage.setBackgroundResource(R.drawable.no_internet_icon)
        binding.searchHistory.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showNothingFoundPlaceholder(){
        binding.progressBar.visibility = View.GONE
        binding.searchError.visibility = View.VISIBLE
        binding.searchErrorButton.visibility = View.GONE
        binding.searchErrorText.setText(R.string.nothing_found)
        binding.searchErrorImage.setBackgroundResource(R.drawable.nothing_found_icon)
        binding.searchHistory.visibility = View.GONE
        binding.recyclerView.visibility = View.GONE
    }

    private fun showLoading(){
        binding.recyclerView.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    //Сохранение введенного в Поиске Текста
    private var searchText = TEXT
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchText = binding.inputEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, TEXT)
        binding.inputEditText.setText(searchText)
    }


    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TEXT = ""
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
