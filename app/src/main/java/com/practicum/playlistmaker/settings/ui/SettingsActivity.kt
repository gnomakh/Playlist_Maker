package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.Creator
import com.practicum.playlistmaker.settings.ui.ViewModel.SettingsViewModel
import com.practicum.playlistmaker.sharing.domain.model.IntentState

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<LinearLayout>(R.id.share_button)
        val supportButton = findViewById<LinearLayout>(R.id.support_button)
        val agreementButton = findViewById<LinearLayout>(R.id.agreement_button)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)


        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]

        backButton.setOnClickListener {
            finish()
        }

        themeSwitch.isChecked = Creator.provideSettingsInteractor().getDarkThemeState()

        themeSwitch.setOnCheckedChangeListener { switch, isChecked ->
            viewModel.switchTheme(isChecked)
        }

        shareButton.setOnClickListener {
            startActivityIfNonNull(IntentState.SHARE)
        }

        supportButton.setOnClickListener {
           startActivityIfNonNull(IntentState.SEND_EMAIL)
        }

        agreementButton.setOnClickListener {
            startActivityIfNonNull(IntentState.OPEN_TERMS)
        }
    }

    private fun startActivityIfNonNull(intentType: IntentState) {
        viewModel.setIntentType(intentType)
        val currentIntent = viewModel.getIntent().value
        if(currentIntent != null) startActivity(currentIntent)
    }
}