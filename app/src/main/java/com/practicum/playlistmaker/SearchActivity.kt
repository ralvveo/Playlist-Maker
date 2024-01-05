package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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



        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val trackList: MutableList<Track> = mutableListOf()
        val trackAdapter = TracksAdapter(trackList)
        recyclerView.adapter = trackAdapter

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

        //Text Watcher
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                clearButton.visibility = clearButtonVisibility(s)

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)





        //Работа c Itunes Search Api
        fun goForApiSearch(){
            val placeholderNothingFound =  findViewById<LinearLayout>(R.id.nothing_found)
            val placeholderNoInternet =  findViewById<LinearLayout>(R.id.no_internet)

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
                                    placeholderNothingFound.visibility = View.GONE
                                    placeholderNoInternet.visibility = View.GONE
                                }
                                if (trackList.isEmpty()) {
                                    placeholderNothingFound.visibility = View.VISIBLE
                                    placeholderNoInternet.visibility = View.GONE
                                    trackList.clear()
                                    trackAdapter.notifyDataSetChanged()
                                }
                            }
                            else {
                                placeholderNothingFound.visibility = View.GONE
                                placeholderNoInternet.visibility = View.VISIBLE

                            }
                        }

                        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                            placeholderNothingFound.visibility = View.GONE
                            placeholderNoInternet.visibility = View.VISIBLE
                        }

                    })
                }


        }

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) goForApiSearch()
            false
        }

        val updateButton = findViewById<Button>(R.id.no_internet_button)
        updateButton.setOnClickListener {
            goForApiSearch()
        }









    }





    //Сохранение введенного в Поиске Текста
    private var searchText = TEXT

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        searchText = inputEditText.text.toString()
        outState.putString(SEARCH_TEXT, searchText)
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
    }


}
