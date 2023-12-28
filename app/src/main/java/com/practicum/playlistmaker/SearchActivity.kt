package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //Кнопка Назад
        val searchButtonBack = findViewById<ImageView>(R.id.search_button_back)
        searchButtonBack.setOnClickListener {

            val displayIntent = Intent(this, MainActivity::class.java)
            startActivity(displayIntent)
        }

        //Видимость Крестика в Поиске

        val inputEditText = findViewById<EditText>(R.id.input_edit_text)
        val clearButton = findViewById<ImageView>(R.id.clear_icon)

        clearButton.setOnClickListener {
            inputEditText.setText("")
        }

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

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val trackNames: List<String> = listOf("Smells Like Teen Spirit", "Billie Jean", "Stayin' Alive", "Whole Lotta Love", "Sweet Child O'Mine")

        val trackArtistNames: List<String> = listOf("Nirvana", "Michael Jackson", "Bee Gees", "Led Zeppelin", "Guns N' Roses")
        val trackTimes: List<String> = listOf("5:01", "4:35", "4:10", "5:33", "5:03")
        val trackImageUrls: List<String> = listOf(
            "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg",
            "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg",
            "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg",
            "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")

        val trackList: MutableList<Track> = mutableListOf()

        for (i in 0..4)
            trackList.add(Track(trackNames[i], trackArtistNames[i], trackTimes[i], trackImageUrls[i]))

        val trackAdapter = TracksAdapter(trackList)
        recyclerView.adapter = trackAdapter







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
