package com.practicum.playlistmaker


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ContextUtils.getActivity

import com.practicum.playlistmaker.App.Companion.trackHistoryList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    //Работа c Itunes Search Api

    private val itunesSearchBaseUrl = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesSearchBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchService = retrofit.create(ItunesSearchApi::class.java)





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Кнопка Назад
        val searchButtonBack = findViewById<ImageView>(R.id.search_button_back)
        searchButtonBack.setOnClickListener {

            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        val searchHistory = findViewById<ScrollView>(R.id.search_history)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistoryObject = SearchHistory(sharedPrefs)
        trackHistoryList = searchHistoryObject.read()
        val trackHistoryAdapter = TracksAdapter(trackHistoryList)
        trackHistoryAdapter.notifyDataSetChanged()






        //Список Треков в Поиске
        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trackList: MutableList<Track> = mutableListOf()
        val trackAdapter = TracksAdapter(trackList)
        recyclerView.adapter = trackAdapter

        //История Поиска

        val searchHistoryList = findViewById<RecyclerView>(R.id.search_history_list)
        searchHistoryList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true)
        searchHistoryList.adapter = trackHistoryAdapter
        trackHistoryAdapter.notifyDataSetChanged()
        clearButton.setOnClickListener {
            inputEditText.setText("")
            trackList.clear()
            trackAdapter.notifyDataSetChanged()


        }







        //Видимость Крестика в Поиске
        fun clearButtonVisibility(s: CharSequence?): Int {

            return if (s.isNullOrEmpty()) {
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                inputMethodManager?.hideSoftInputFromWindow(clearButton.windowToken, 0)
                View.GONE


            } else {
                View.VISIBLE
            }
        }
        //Видимость Истории Поиска
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            searchHistory.visibility = if (hasFocus && inputEditText.text.isEmpty() && trackHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
            trackHistoryAdapter.notifyDataSetChanged()
            trackAdapter.notifyDataSetChanged()
        }

        val clearHistoryButton = findViewById<Button>(R.id.search_history_button)
        clearHistoryButton.setOnClickListener{
            searchHistoryObject.clear()
            trackHistoryList.clear()
            trackHistoryAdapter.notifyDataSetChanged()
            searchHistory.visibility = View.GONE
        }

        //Text Watcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s)
                searchHistory.visibility = if (inputEditText.hasFocus() && s?.isEmpty() == true && trackHistoryList.isNotEmpty()) View.VISIBLE else View.GONE
                trackHistoryAdapter.notifyDataSetChanged()
                if (s?.isEmpty() == true){
                    trackList.clear()
                    trackAdapter.notifyDataSetChanged()
                }

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)






        //Работа c Itunes Search Api
        fun goForApiSearch(){
            val placeholderError =  findViewById<LinearLayout>(R.id.search_error)
            val placeholderErrorText = findViewById<TextView>(R.id.search_error_text)
            val placeholderErrorImage = findViewById<ImageView>(R.id.search_error_image)
            val placeholderErrorButton = findViewById<Button>(R.id.search_error_button)

                if (inputEditText.text.isNotEmpty()) {
                    itunesSearchService.search(inputEditText.text.toString()).enqueue(object :
                        Callback<TracksResponse> {
                        override fun onResponse(call: Call<TracksResponse>,
                                                response: Response<TracksResponse>
                        ) {
                            if (response.code() == 200) {
                                trackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    trackList.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                    placeholderError.visibility = View.GONE
                                }
                                if (trackList.isEmpty()) {
                                    placeholderError.visibility = View.VISIBLE
                                    placeholderErrorButton.visibility = View.GONE
                                    placeholderErrorText.setText(R.string.nothing_found)
                                    placeholderErrorImage.setBackgroundResource(R.drawable.nothing_found_icon)
                                    trackList.clear()
                                    trackAdapter.notifyDataSetChanged()
                                }
                            }
                            else {
                                placeholderError.visibility = View.VISIBLE
                                placeholderErrorButton.visibility = View.VISIBLE
                                placeholderErrorText.setText(R.string.no_internet_text)
                                placeholderErrorImage.setBackgroundResource(R.drawable.no_internet_icon)

                            }
                        }

                        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                            placeholderError.visibility = View.VISIBLE
                            placeholderErrorButton.visibility = View.VISIBLE
                            placeholderErrorText.setText(R.string.no_internet_text)
                            placeholderErrorImage.setBackgroundResource(R.drawable.no_internet_icon)
                        }

                    })
                }


        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) goForApiSearch()
            false
        }

        val updateButton = findViewById<Button>(R.id.search_error_button)
        updateButton.setOnClickListener {
            goForApiSearch()
        }









    }
    val trackHistoryAdapter = TracksAdapter(trackHistoryList)






    //Сохранение введенного в Поиске Текста
    private var searchText = TEXT


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        searchText = inputEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistoryObject = SearchHistory(sharedPrefs)
        searchHistoryObject.write(trackHistoryList)
        trackHistoryAdapter.notifyDataSetChanged()

    }

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TEXT = ""
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchText = savedInstanceState.getString(SEARCH_TEXT, TEXT)
        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        inputEditText.setText(searchText)
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val searchHistoryObject = SearchHistory(sharedPrefs)
        trackHistoryList = searchHistoryObject.read()
        trackHistoryAdapter.notifyDataSetChanged()
    }






}
