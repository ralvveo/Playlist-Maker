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
