package com.practicum.playlistmaker.settings.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.ViewModel.SettingsViewModel
import com.practicum.playlistmaker.settings.domain.model.IntentState
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val shareButton = findViewById<LinearLayout>(R.id.share_button)
        val supportButton = findViewById<LinearLayout>(R.id.support_button)
        val agreementButton = findViewById<LinearLayout>(R.id.agreement_button)
        val themeSwitch = findViewById<SwitchMaterial>(R.id.theme_switch)

        backButton.setOnClickListener {
            finish()
        }

        themeSwitch.isChecked = viewModel.getSwitchState()

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
        startActivity(viewModel.getIntentState().value)
    }
}