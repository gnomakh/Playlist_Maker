package com.practicum.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settingsInteractor = Creator.provideSettingsInteractor()

        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<LinearLayout>(R.id.share_button)
        val supportButton = findViewById<LinearLayout>(R.id.support_button)
        val agreementButton = findViewById<LinearLayout>(R.id.agreement_button)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)

        backButton.setOnClickListener {
            finish()
        }

        themeSwitch.isChecked = Creator.provideSettingsInteractor().getDarkThemeState()

        themeSwitch.setOnCheckedChangeListener { switch, isChecked ->
            settingsInteractor.saveDarkThemeState(isChecked)
            (applicationContext as App).switchTheme(isChecked)
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
