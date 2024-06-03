package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId") // Потом уберу
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val searchButton = findViewById<Button>(R.id.search_button)
        val mediaButton = findViewById<Button>(R.id.media_button)
        val settingsButton =  findViewById<Button>(R.id.settings_button)

        val searchButtonOnClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                Toast.makeText(this@MainActivity, "ТЫ НАЖАЛ ПОИСК!!!", Toast.LENGTH_SHORT).show()
            }
        }
        searchButton.setOnClickListener(searchButtonOnClickListener)

        mediaButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "ТЫ НАЖАЛ МЕДИАТЕКУ!!!", Toast.LENGTH_SHORT).show()
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this@MainActivity, "ТЫ НАЖАЛ НАСТРОЙКИ!!!", Toast.LENGTH_SHORT).show()
        }

    }
}