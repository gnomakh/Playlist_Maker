package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<LinearLayout>(R.id.share_button)
        val supportButton = findViewById<LinearLayout>(R.id.support_button)
        val agreementButton = findViewById<LinearLayout>(R.id.agreement_button)

        val themeSwitch = findViewById<Switch>(R.id.theme_switch)
        val sharedPreferences = getSharedPreferences("Mode", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val nightMode = sharedPreferences.getBoolean("night", false)

        if(nightMode) {
            themeSwitch.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        backButton.setOnClickListener{
            finish()
        }

        themeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean("night", false)
                editor.apply()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean("night", true)
                editor.apply()
            }
        }

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, getString(R.string.course_link))
            }
            startActivity(shareIntent)
        }

        supportButton.setOnClickListener {
            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.dev_email)))
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.email_text))
            }
            startActivity(supportIntent)
        }

        agreementButton.setOnClickListener {
            val url = Uri.parse(getString(R.string.agreement_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(agreementIntent)
        }
    }
}