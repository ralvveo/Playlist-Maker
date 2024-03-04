package com.practicum.playlistmaker._unsorted.ui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker._unsorted_data.network.ItunesSearchApi
import com.practicum.playlistmaker._unsorted.PLAYLIST_MAKER_PREFERENCES
import com.practicum.playlistmaker._unsorted.creator.Creator
import com.practicum.playlistmaker._unsorted_data.dto.TracksResponse
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker._unsorted_domain.model.Track
import com.practicum.playlistmaker._unsorted_domain.repository.SearchHistoryFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchHistory: SearchHistoryFunctions
    //Работа c Itunes Search Api
    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TEXT = ""
        var trackHistoryList = mutableListOf<Track>() //История Поиска
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private val itunesSearchBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchService = retrofit.create(ItunesSearchApi::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory = Creator.provideSearchHistoryFunctions(sharedPrefs)
        //Кнопка Назад
        binding.searchButtonBack.setOnClickListener {
            finishAfterTransition()
        }

        //Чтение Истории Поиска из Shared Preferences и отображение ее на экране
        trackHistoryList = searchHistory.read()
        val trackHistoryAdapter = TracksAdapter(trackHistoryList)
        binding.searchHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.searchHistoryList.adapter = trackHistoryAdapter
        trackHistoryAdapter.notifyDataSetChanged()


        //Список Треков в Поиске
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trackList: MutableList<Track> = mutableListOf()
        val trackAdapter = TracksAdapter(trackList)
        binding.recyclerView.adapter = trackAdapter
        binding.clearButton.setOnClickListener {
            binding.inputEditText.setText("")
            trackList.clear()
            trackAdapter.notifyDataSetChanged()


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
            binding.searchHistory.visibility = if (hasFocus && binding.inputEditText.text.isEmpty() && trackHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
            trackAdapter.notifyDataSetChanged()
        }

        binding.searchHistoryButton.setOnClickListener{
            searchHistory.clear()
            trackHistoryList.clear()
            trackHistoryAdapter.notifyDataSetChanged()
            binding.searchHistory.visibility = View.GONE
        }

        //Text Watcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                binding.clearButton.visibility = clearButtonVisibility(s)
                binding.searchHistory.visibility = if (binding.inputEditText.hasFocus() && s?.isEmpty() == true && trackHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
                trackHistoryAdapter.notifyDataSetChanged()
                if (s?.isEmpty() == true){
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE

                }

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }

        binding.inputEditText.addTextChangedListener(simpleTextWatcher)



        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) goForApiSearch()
            false
        }

        binding.searchErrorButton.setOnClickListener {
            goForApiSearch()
        }

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

    override fun onPause() {
        super.onPause()
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        searchHistory.write(trackHistoryList)


    }

    override fun onResume() {
        super.onResume()
        //Чтение Истории Поиска из Shared Preferences и отображение ее на экране
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        trackHistoryList = searchHistory.read()
        val trackHistoryAdapter = TracksAdapter(trackHistoryList)
        binding.searchHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        binding.searchHistoryList.adapter = trackHistoryAdapter
        trackHistoryAdapter.notifyDataSetChanged()
    }



    //Работа c Itunes Search Api
    private fun goForApiSearch(){
        val trackList: MutableList<Track> = mutableListOf()
        val trackAdapter = TracksAdapter(trackList)
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = trackAdapter
        if (binding.inputEditText.text.isNotEmpty()) {
            itunesSearchService.search(binding.inputEditText.text.toString()).enqueue(object :
                Callback<TracksResponse> {
                override fun onResponse(call: Call<TracksResponse>,
                                        response: Response<TracksResponse>
                ) {
                    binding.progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        trackList.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackList.addAll(response.body()?.results!!)
                            trackAdapter.notifyDataSetChanged()
                            binding.searchError.visibility = View.GONE
                        }
                        if (trackList.isEmpty()) {
                            binding.searchError.visibility = View.VISIBLE
                            binding.searchErrorButton.visibility = View.GONE
                            binding.searchErrorText.setText(R.string.nothing_found)
                            binding.searchErrorImage.setBackgroundResource(R.drawable.nothing_found_icon)
                            trackList.clear()
                            trackAdapter.notifyDataSetChanged()
                        }
                    }
                    else {
                        binding.progressBar.visibility = View.GONE
                        binding.searchError.visibility = View.VISIBLE
                        binding.searchErrorButton.visibility = View.VISIBLE
                        binding.searchErrorText.setText(R.string.no_internet_text)
                        binding.searchErrorImage.setBackgroundResource(R.drawable.no_internet_icon)

                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    binding.searchError.visibility = View.VISIBLE
                    binding.searchErrorButton.visibility = View.VISIBLE
                    binding.searchErrorText.setText(R.string.no_internet_text)
                    binding.searchErrorImage.setBackgroundResource(R.drawable.no_internet_icon)
                }

            })
        }
        else{
            binding.progressBar.visibility = View.GONE
        }
    }

    //Реализация автоматического поиска через несколько секунд
    private val handler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {goForApiSearch()}
    fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }





}
